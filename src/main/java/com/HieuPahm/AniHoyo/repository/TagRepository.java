package com.HieuPahm.AniHoyo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.HieuPahm.AniHoyo.dtos.TagDTO;
import com.HieuPahm.AniHoyo.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {
    Set<TagDTO> findByIdIn(List<Long> id);
}
