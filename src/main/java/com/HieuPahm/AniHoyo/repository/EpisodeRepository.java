package com.HieuPahm.AniHoyo.repository;

import com.HieuPahm.AniHoyo.entities.Episode;
import com.HieuPahm.AniHoyo.entities.Season;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, String> {
    Optional<Episode> findByTitle(String title);
    // query methods

    List<Episode> findBySeason(Season season);
}
