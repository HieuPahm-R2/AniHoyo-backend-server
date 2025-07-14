package com.HieuPahm.AniHoyo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.HieuPahm.AniHoyo.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
