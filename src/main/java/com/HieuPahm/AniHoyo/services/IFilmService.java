package com.HieuPahm.AniHoyo.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.HieuPahm.AniHoyo.dtos.FilmDTO;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.entities.Film;

public interface IFilmService extends ICrudService<FilmDTO,Long> {
    FilmDTO getFilmBySeasonId(Long id);
    PaginationResultDTO getAll(Specification<Film> spec,Pageable pageable);
}
