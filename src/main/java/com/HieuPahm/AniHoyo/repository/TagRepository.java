package com.HieuPahm.AniHoyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HieuPahm.AniHoyo.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    
}
