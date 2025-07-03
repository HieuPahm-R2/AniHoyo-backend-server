package com.HieuPahm.AniHoyo.services;

import com.HieuPahm.AniHoyo.entities.Episode;
import org.springframework.web.multipart.MultipartFile;

public interface EpisodeService {
    // save video
    Episode saveVideo(Episode episode, MultipartFile multipartFile);
    // get video by id
    Episode getVideo(String videoId);
    // get video with title
    Episode getVideoByTitle(String title);


}
