package com.HieuPahm.AniHoyo.services;

import com.HieuPahm.AniHoyo.dtos.FilmDTO;
import com.HieuPahm.AniHoyo.dtos.UploadFilmDTO;

public interface IFilmService extends ICrudService<FilmDTO,Long> {
    UploadFilmDTO upload(UploadFilmDTO uploadFlimDTO);
    UploadFilmDTO getFlimForUpload(Long id);
}
