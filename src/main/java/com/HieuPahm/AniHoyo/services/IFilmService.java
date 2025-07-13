package com.HieuPahm.AniHoyo.services;

import com.HieuPahm.AniHoyo.dtos.UploadFilmDTO;

public interface IFilmService {
    UploadFilmDTO upload(UploadFilmDTO uploadFlimDTO);
    UploadFilmDTO getFlimForUpload(Long id);
}
