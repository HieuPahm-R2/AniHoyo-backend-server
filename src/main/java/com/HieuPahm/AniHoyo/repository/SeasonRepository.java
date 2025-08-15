package com.HieuPahm.AniHoyo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.HieuPahm.AniHoyo.entities.Film;
import com.HieuPahm.AniHoyo.entities.Season;

public interface SeasonRepository extends JpaRepository<Season, Long>, JpaSpecificationExecutor<Season> {
    List<Season> findByFilm(Film film);
}
