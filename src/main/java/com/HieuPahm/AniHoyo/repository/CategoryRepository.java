package com.HieuPahm.AniHoyo.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.HieuPahm.AniHoyo.dtos.CategoryDTO;
import com.HieuPahm.AniHoyo.entities.Category;


public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    Set<CategoryDTO> findByIdIn(List<Long> id);
}
