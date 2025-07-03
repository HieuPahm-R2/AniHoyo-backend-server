package com.HieuPahm.AniHoyo.controller;

import com.HieuPahm.AniHoyo.entities.Episode;
import com.HieuPahm.AniHoyo.playload.CustomMessage;
import com.HieuPahm.AniHoyo.services.EpisodeService;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/episodes")
public class EpisodeController {
    private final EpisodeService episodeService;
    public EpisodeController( EpisodeService episodeService){
        this.episodeService = episodeService;
    }
    @PostMapping("")
    public ResponseEntity<?> createEpisode(
            @RequestParam("file")MultipartFile file, @RequestParam("title") String title,
            @RequestParam("description") String description){
        Episode ep = new Episode();
        ep.setTitle(title);
        ep.setDescription(description);
        ep.setId(UUID.randomUUID().toString());
        Episode savedEpisode = episodeService.saveVideo(ep,file);
        if(savedEpisode != null){
            return ResponseEntity.status(HttpStatus.OK).body(ep);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomMessage.builder().message("Can not upload video").success(false).build());
        }
    }
}
