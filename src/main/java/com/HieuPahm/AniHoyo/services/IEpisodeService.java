package com.HieuPahm.AniHoyo.services;

import com.HieuPahm.AniHoyo.model.dtos.EpisodeDTO;

public interface IEpisodeService extends ICrudService<EpisodeDTO, Long> {
    public long processVideo(long videoId);
}
