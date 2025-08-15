package com.HieuPahm.AniHoyo.controller;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HieuPahm.AniHoyo.dtos.CategoryDTO;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.entities.Category;
import com.HieuPahm.AniHoyo.repository.CategoryRepository;
import com.HieuPahm.AniHoyo.services.implement.CategoryService;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/add-category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.insert(categoryDTO));
    }

    @PutMapping("/update-category")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return ResponseEntity.ok("update this category completely");
    }

    @DeleteMapping("/delete-category/{id}")
    @MessageApi("Delete category")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws BadActionException {
        if (this.categoryRepository.findById(id).isEmpty()) {
            throw new BadActionException("Not Found this ID");
        }
        this.categoryService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/get-all-category")
    @MessageApi("Fetch all categories with pagination")
    public ResponseEntity<PaginationResultDTO> getAllCategories(@Filter Specification<Category> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.categoryService.getAll(spec, pageable));
    }
}
