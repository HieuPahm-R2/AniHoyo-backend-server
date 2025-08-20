package com.HieuPahm.AniHoyo.controller;

import com.HieuPahm.AniHoyo.dtos.EpisodeDTO;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.ResUpFileDTO;
import com.HieuPahm.AniHoyo.repository.EpisodeRepository;
import com.HieuPahm.AniHoyo.services.implement.EpisodeService;
import com.HieuPahm.AniHoyo.services.implement.FileServiceImpl;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.HieuPahm.AniHoyo.utils.constant.ChunkConstant;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;
import com.HieuPahm.AniHoyo.utils.error.StorageException;

import jakarta.validation.Valid;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
public class EpisodeController {
    @Value("${hieupham.upload-file.base-uri}")
    private String baseURI;

    private final EpisodeService episodeService;
    private final EpisodeRepository episodeRepository;
    private final FileServiceImpl fileServiceImpl;

    public EpisodeController(EpisodeService episodeService, EpisodeRepository episodeRepository,
            FileServiceImpl fileServiceImpl) {
        this.episodeService = episodeService;
        this.episodeRepository = episodeRepository;
        this.fileServiceImpl = fileServiceImpl;
    }

    @PostMapping("/add-episode")
    public ResponseEntity<?> createEpisode(@Valid @RequestBody EpisodeDTO dto) throws BadActionException {
        if (this.episodeService.checkExistSeason(dto.getSeason().getId()) == false) {
            throw new BadActionException("Not Found this season");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.episodeService.insert(dto));
    }

    @PostMapping("/update-episode")
    public ResponseEntity<?> update(@Valid @RequestBody EpisodeDTO dto) throws BadActionException {
        return ResponseEntity.ok().body(this.episodeService.update(dto));
    }

    @GetMapping("/episode/{id}")
    public ResponseEntity<EpisodeDTO> watch(@PathVariable("id") long id) throws BadActionException {
        if (this.episodeRepository.findById(id).isEmpty()) {
            throw new BadActionException("Not Found this episode");
        }
        return ResponseEntity.ok().body(this.episodeService.getById(id));
    }

    @DeleteMapping("/delete-episode/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws BadActionException {
        if (this.episodeRepository.findById(id).isEmpty()) {
            throw new BadActionException("Not Found this episode");
        }
        this.episodeService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/episodes/by-season/{seasonId}")
    @MessageApi("fetch all eps of season")
    public ResponseEntity<PaginationResultDTO> fetchEpsBySeason(@PathVariable Long seasonId, Pageable pageable) {
        return ResponseEntity.ok().body(this.episodeService.fetchEpsBySeason(seasonId, pageable));
    }

    @PostMapping("/upload/video")
    @MessageApi("Upload an Episode")
    public ResponseEntity<ResUpFileDTO> uploadEpisode(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        // CHECK VALIDATE
        if (file == null || file.isEmpty()) {
            throw new StorageException("Not leave blank, Please upload!!");
        }
        String fileName = file.getOriginalFilename();
        List<String> extensionsAllowed = Arrays.asList("mp4", "mkv", "avi", "mov", "webm", "flv", "wmv");
        boolean isValid = extensionsAllowed.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new StorageException("Invalid file format, Please try again!");
        }
        // handle create folder (Option)
        this.fileServiceImpl.createFolder(baseURI + folder);
        String fileUpload = this.fileServiceImpl.storeFile(file, folder);
        ResUpFileDTO result = new ResUpFileDTO(fileUpload, Instant.now());

        return ResponseEntity.ok().body(result);
    }
    // streaming video with chunked method

}
