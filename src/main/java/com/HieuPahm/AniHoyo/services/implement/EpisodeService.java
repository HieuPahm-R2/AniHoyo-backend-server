package com.HieuPahm.AniHoyo.services.implement;

import com.HieuPahm.AniHoyo.dtos.EpisodeDTO;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.SeasonDTO;
import com.HieuPahm.AniHoyo.entities.Episode;
import com.HieuPahm.AniHoyo.entities.Season;
import com.HieuPahm.AniHoyo.repository.EpisodeRepository;
import com.HieuPahm.AniHoyo.repository.SeasonRepository;
import com.HieuPahm.AniHoyo.services.IEpisodeService;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import jakarta.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        // process video
        Episode ep = this.episodeRepository.save(modelMapper.map(dto, Episode.class));
        processVideo(ep.getId());
        return modelMapper.map(ep, EpisodeDTO.class);
    }

    @Override
    public EpisodeDTO getById(Long id) {
        Optional<Episode> ep = this.episodeRepository.findById(id);
        return modelMapper.map(ep.get(), EpisodeDTO.class);
    }

    @Override
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
    public void delete(Long id) {
        this.episodeRepository.deleteById(id);
    }

    public PaginationResultDTO fetchEpsBySeason(Long seasonId, Pageable pageable) {
        // Tạo filter để lấy season theo filmId
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

        URI uri2 = URI.create(baseURI + "videos/" + filePath);
        Path videoPath = Paths.get(uri2);

        try {
            URI uri = URI.create(baseURI + "videos_hls/" + episode.getTitle());
            Path outputPath = Paths.get(uri);

            Files.createDirectories(outputPath);

            Path segmentPath = outputPath.resolve("segment_%3d.ts");
            Path playlistPath = outputPath.resolve("master.m3u8");

            ProcessBuilder processBuilder = new ProcessBuilder(
                    ffmpegPath,
                    "-nostdin",
                    "-y",
                    "-i", videoPath.toString(),
                    "-c:v", "libx264",
                    "-c:a", "aac",
                    "-f", "hls",
                    "-hls_time", "10",
                    "-hls_list_size", "0",
                    "-hls_segment_filename", segmentPath.toString(),
                    playlistPath.toString()
            );
            System.out.println(String.join(" ", processBuilder.command()));
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
            return id;

        } catch (IOException ex) {
            throw new RuntimeException("processing failed !!\n" + ex.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
