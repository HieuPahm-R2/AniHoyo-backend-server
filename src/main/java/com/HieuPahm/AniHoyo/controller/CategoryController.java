package com.HieuPahm.AniHoyo.controller;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HieuPahm.AniHoyo.dtos.CategoryDTO;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.entities.Category;
import com.HieuPahm.AniHoyo.services.implement.CategoryService;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }
    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok(categoryService.insert(categoryDTO));
    }
    @PutMapping("/update-category")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO){
        categoryService.update(categoryDTO);
        return ResponseEntity.ok("update this category completely");
    }
    @GetMapping("/get-all-category")
    @MessageApi("Fetch all categories with pagination")
    public ResponseEntity<PaginationResultDTO> getAllCategories(@Filter Specification<Category> spec, Pageable pageable){
        return ResponseEntity.ok().body(this.categoryService.getAll(spec, pageable));
    }
}
