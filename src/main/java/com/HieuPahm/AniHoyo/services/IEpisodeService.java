package com.HieuPahm.AniHoyo.services;

import com.HieuPahm.AniHoyo.dtos.EpisodeDTO;

public interface IEpisodeService extends ICrudService<EpisodeDTO, Long> {
    public long processVideo(long videoId);
}
