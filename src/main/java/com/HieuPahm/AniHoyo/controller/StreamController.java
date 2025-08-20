package com.HieuPahm.AniHoyo.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HieuPahm.AniHoyo.entities.Episode;
import com.HieuPahm.AniHoyo.repository.EpisodeRepository;
import com.HieuPahm.AniHoyo.services.implement.EpisodeService;
import com.HieuPahm.AniHoyo.services.implement.FileServiceImpl;
import com.HieuPahm.AniHoyo.utils.constant.ChunkConstant;

@RestController
@RequestMapping("/api/v1")
public class StreamController {

    @Value("${hieupham.upload-file.base-uri}")
    private String baseURI;

    private final EpisodeRepository episodeRepository;
    private final FileServiceImpl fileServiceImpl;

    public StreamController(EpisodeService episodeService, EpisodeRepository episodeRepository,
            FileServiceImpl fileServiceImpl) {

        this.episodeRepository = episodeRepository;
        this.fileServiceImpl = fileServiceImpl;
    }

    @GetMapping("/stream/range/{id}")
    public ResponseEntity<?> streamVideoRange(
            @PathVariable("id") long id,
            @RequestHeader(value = "Range", required = false) String range) {

        Optional<Episode> episode = this.episodeRepository.findById(id);
        if (episode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        URI uri = URI.create(baseURI + "videos/" + episode.get().getFilePath());
        Path path = Paths.get(uri);

        if (!Files.exists(path)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String contentType = episode.get().getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        long fileLength;
        try {
            fileLength = Files.size(path);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // nếu client không gửi range thì trả nguyên file
        if (range == null) {
            Resource resource = new FileSystemResource(path);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(fileLength)
                    .body(resource);
        }
        // --- xử lý Range header ---
        long rangeStart;
        long rangeEnd;

        String[] ranges = range.replace("bytes=", "").split("-");
        rangeStart = Long.parseLong(ranges[0]);

        if (ranges.length > 1 && !ranges[1].isEmpty()) {
            rangeEnd = Long.parseLong(ranges[1]);
        } else {
            rangeEnd = rangeStart + ChunkConstant.CHUNK_SIZE - 1;
        }
        if (rangeEnd >= fileLength) {
            rangeEnd = fileLength - 1;
        }
        // nếu file rỗng thì trả lỗi
        if (fileLength <= 0 || rangeStart > rangeEnd) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            inputStream.skip(rangeStart);
            long contentLength = rangeEnd - rangeStart + 1;
            byte[] data = new byte[(int) contentLength];
            int read = inputStream.read(data, 0, data.length);

            System.out.println("range start : " + rangeStart);
            System.out.println("range end   : " + rangeEnd);
            System.out.println("read bytes  : " + read);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileLength);
            headers.setContentLength(contentLength);
            headers.add("Accept-Ranges", "bytes");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(new ByteArrayResource(data));

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/master.m3u8")
    public ResponseEntity<Resource> serverMasterFile(
            @PathVariable("id") long id) {
        Optional<Episode> episode = this.episodeRepository.findById(id);
        if (episode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Path path = Paths.get(baseURI, episode.get().getTitle(), "master.m3u8");

        if (!Files.exists(path)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Resource resource = new FileSystemResource(path);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.apple.mpegurl")
                .body(resource);
    }

    // serve the segments
    @GetMapping("/{id}/{segment}.ts")
    public ResponseEntity<Resource> serveSegments(
            @PathVariable("id") long id,
            @PathVariable String segment) {
        Optional<Episode> episode = this.episodeRepository.findById(id);
        if (episode.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        // create path for segment
        Path path = Paths.get(baseURI, episode.get().getTitle(), segment + ".ts");
        if (!Files.exists(path)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Resource resource = new FileSystemResource(path);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "video/mp2t")
                .body(resource);
    }
}
