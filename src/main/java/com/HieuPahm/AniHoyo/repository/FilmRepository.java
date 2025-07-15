package com.HieuPahm.AniHoyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HieuPahm.AniHoyo.entities.Film;

public interface FilmRepository extends JpaRepository<Film, Long> {
     
}
