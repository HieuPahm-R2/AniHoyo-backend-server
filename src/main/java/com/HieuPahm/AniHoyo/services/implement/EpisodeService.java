package com.HieuPahm.AniHoyo.services.implement;

import com.HieuPahm.AniHoyo.model.dtos.EpisodeDTO;
import com.HieuPahm.AniHoyo.model.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.model.entities.Episode;
import com.HieuPahm.AniHoyo.model.entities.Season;
import com.HieuPahm.AniHoyo.repository.EpisodeRepository;
import com.HieuPahm.AniHoyo.repository.SeasonRepository;
import com.HieuPahm.AniHoyo.services.IEpisodeService;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EpisodeService implements IEpisodeService {
    @Autowired
    FilterBuilder fb;
    @Autowired
    private FilterParser filterParser;
    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;
    @Value("${hieupham.upload-file.base-uri}")
    private String baseURI;
    @Value("${hieupham.ffmpeg-path:ffmpeg}")
    private String ffmpegPath;
    @Value("${hieupham.ffprobe-path:ffprobe}")
    private String ffprobePath;

    private final EpisodeRepository episodeRepository;
    private final SeasonRepository seasonRepository;
    private final ModelMapper modelMapper;

    public EpisodeService(EpisodeRepository episodeRepository, SeasonRepository seasonRepository,
            ModelMapper modelMapper) {
        this.episodeRepository = episodeRepository;
        this.seasonRepository = seasonRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EpisodeDTO insert(EpisodeDTO dto) {
        Episode ep = this.episodeRepository.save(modelMapper.map(dto, Episode.class));
        processVideo(ep.getId());
        return modelMapper.map(ep, EpisodeDTO.class);
    }

    @Override
    @Cacheable(value = "episodes", key = "#id")
    public EpisodeDTO getById(Long id) {
        Optional<Episode> ep = this.episodeRepository.findById(id);
        return modelMapper.map(ep.get(), EpisodeDTO.class);
    }

    @Override
    @CacheEvict(value = "episodes", key = "#dto.id")
    public EpisodeDTO update(EpisodeDTO dto) throws BadActionException {
        Optional<Episode> ep = this.episodeRepository.findById(dto.getId());
        if (ep.isPresent()) {
            ep.get().setTitle(dto.getTitle());
            ep.get().setFilePath(dto.getFilePath());
            ep.get().setContentType(dto.getContentType());
            this.episodeRepository.save(ep.get());
        }
        throw new BadActionException("Not Found this ep");
    }

    @Override
    @CacheEvict(value = "episodes", key = "#id")
    public void delete(Long id) {
        this.episodeRepository.deleteById(id);
    }

    public PaginationResultDTO fetchEpsBySeason(Long seasonId, Pageable pageable) {
        FilterNode node = filterParser.parse("season.id=" + seasonId);
        FilterSpecification<Episode> spec = filterSpecificationConverter.convert(node);
        Page<Episode> pageCheck = this.episodeRepository.findAll(spec, pageable);

        PaginationResultDTO ans = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();

        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setTotal(pageCheck.getTotalElements());
        mt.setPages(pageCheck.getTotalPages());
        ans.setMeta(mt);

        List<EpisodeDTO> res = pageCheck.getContent().stream()
                .map(item -> modelMapper.map(item, EpisodeDTO.class))
                .collect(Collectors.toList());
        ans.setResult(res);
        return ans;
    }

    public boolean checkExistSeason(Long id) {
        Optional<Season> check = this.seasonRepository.findById(id);
        if (check.isEmpty()) {
            return false;
        }
        return true;
    }

    @Override
    public long processVideo(long id) {
        Episode episode = this.episodeRepository.findById(id).get();
        String filePath = episode.getFilePath();

        URI inputUri = URI.create(baseURI + "videos/" + filePath);
        Path inputVideoPath = Paths.get(inputUri);

        Path outputRoot = Paths.get(URI.create(baseURI + "videos_hls/" + episode.getTitle() + "/"));

        try {
            Files.createDirectories(outputRoot);

            int sourceWidth = 0;
            int sourceHeight = 0;
            try {
                int[] wh = probeVideoResolution(inputVideoPath);
                sourceWidth = wh[0];
                sourceHeight = wh[1];
            } catch (Exception ignore) {
                // keep defaults (0,0) to fall back to 360p only
            }

            List<Integer> targetHeights = new ArrayList<>();
            targetHeights.add(360);
            if (sourceHeight >= 720) {
                targetHeights.add(720);
                targetHeights.add(1080);
            }

            List<String> masterEntries = new ArrayList<>();
            for (Integer height : targetHeights) {
                Path variantDir = outputRoot.resolve(height + "p");
                Files.createDirectories(variantDir);

                int outWidth;
                if (sourceWidth > 0 && sourceHeight > 0) {
                    outWidth = Math.max(2, ((sourceWidth * height) / sourceHeight) & ~1);
                } else {
                    outWidth = Math.max(2, ((16 * height) / 9) & ~1);
                }

                Path segmentPath = variantDir.resolve("segment_%03d.ts");
                Path playlistPath = variantDir.resolve("index.m3u8");

                runFfmpegToHls(inputVideoPath, segmentPath, playlistPath, height);

                long bandwidth;
                if (height >= 1080) {
                    bandwidth = 5000000L;
                } else if (height >= 720) {
                    bandwidth = 2500000L;
                } else {
                    bandwidth = 800000L;
                }
                masterEntries.add("#EXT-X-STREAM-INF:BANDWIDTH=" + bandwidth + ",RESOLUTION=" + outWidth + "x" + height
                        + "\n" + height + "p/index.m3u8");
            }

            StringBuilder master = new StringBuilder();
            master.append("#EXTM3U\n");
            master.append("#EXT-X-VERSION:3\n");
            for (String entry : masterEntries) {
                master.append(entry).append("\n");
            }
            Files.write(outputRoot.resolve("master.m3u8"), master.toString().getBytes(StandardCharsets.UTF_8));

            return id;

        } catch (IOException ex) {
            throw new RuntimeException("processing failed !!\n" + ex.getMessage());
        }
    }

    private int[] probeVideoResolution(Path inputVideoPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                ffprobePath,
                "-v", "error",
                "-select_streams", "v:0",
                "-show_entries", "stream=width,height",
                "-of", "csv=p=0:s=x",
                inputVideoPath.toString());
        pb.redirectErrorStream(true);
        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }
        int exit = process.waitFor();
        if (exit != 0) {
            throw new IOException("ffprobe failed: " + output);
        }
        String result = output.toString().trim();
        String[] parts = result.split("x");
        int width = Integer.parseInt(parts[0].trim());
        int height = Integer.parseInt(parts[1].trim());
        return new int[] { width, height };
    }

    private void runFfmpegToHls(Path inputVideoPath, Path segmentPath, Path playlistPath, int targetHeight)
            throws IOException {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    ffmpegPath,
                    "-nostdin",
                    "-y",
                    "-i", inputVideoPath.toString(),
                    "-vf", "scale=-2:" + targetHeight,
                    "-c:v", "libx264",
                    "-preset", "veryfast",
                    "-crf", "23",
                    "-c:a", "aac",
                    "-f", "hls",
                    "-hls_time", "10",
                    "-hls_list_size", "0",
                    "-hls_playlist_type", "vod",
                    "-hls_segment_filename", segmentPath.toString(),
                    playlistPath.toString());
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append(System.lineSeparator());
                }
            }

            int exit = process.waitFor();
            if (exit != 0) {
                String msg = output.length() > 0 ? output.toString() : "unknown error";
                throw new RuntimeException("processing failed !!\n" + msg);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
