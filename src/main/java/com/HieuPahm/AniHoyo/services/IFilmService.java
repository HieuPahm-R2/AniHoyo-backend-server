package com.HieuPahm.AniHoyo.services;

import java.util.List;

import com.HieuPahm.AniHoyo.dtos.FilmDTO;

public interface IFilmService extends ICrudService<FilmDTO,Long> {
    List<FilmDTO> getAllByCategoryId(Long id);
    FilmDTO getFilmBySeasonId(Long id);

}
