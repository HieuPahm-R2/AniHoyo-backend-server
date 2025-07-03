package com.HieuPahm.AniHoyo.services.implement;

import com.HieuPahm.AniHoyo.entities.Episode;
import com.HieuPahm.AniHoyo.repository.EpisodeRepository;
import com.HieuPahm.AniHoyo.services.EpisodeService;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;
    public EpisodeServiceImpl(EpisodeRepository episodeRepository){
        this.episodeRepository = episodeRepository;
    }

    @Value("${files.video}")
    String Dir;
    @PostConstruct
    public void initFolder(){
        File file = new File(Dir);
        if(!file.exists()){
            file.mkdirs();
            System.out.println("create folder done!");
        }else{
            System.out.println("folder has been created");
        }
    }

    @Override
    public Episode saveVideo(Episode episode, MultipartFile multipartFile) {

        try{
            String fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            InputStream inputStream = multipartFile.getInputStream();
            // file path
            String cleanFileName = StringUtils.cleanPath(fileName);
            // folder path ==> create
            String cleanFolder = StringUtils.cleanPath(Dir);
            // folder path with fileName
            Path path = Paths.get(cleanFolder, cleanFileName);
            //copy file to the folder
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);

            //video metadata
            episode.setContentType(contentType);
            episode.setFilePath(path.toString());

            //metadata save
            return episodeRepository.save(episode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    
    }

    @Override
    public Episode getVideo(String videoId) {
        return null;
    }

    @Override
    public Episode getVideoByTitle(String title) {
        return null;
    }
}
