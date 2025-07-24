package com.HieuPahm.AniHoyo.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.HieuPahm.AniHoyo.dtos.CategoryDTO;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.entities.Category;


public interface ICategoryService extends ICrudService<CategoryDTO, Long> {
    PaginationResultDTO getAll(Specification<Category> spec,Pageable pageable);
}
