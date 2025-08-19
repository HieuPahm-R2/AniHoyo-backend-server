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

import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.TagDTO;
import com.HieuPahm.AniHoyo.entities.Tag;
import com.HieuPahm.AniHoyo.services.implement.TagService;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.turkraft.springfilter.boot.Filter;

@RestController
@RequestMapping("/api/v1")
public class TagController {
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping("/add-tag")
    public ResponseEntity<?> addCategory(@RequestBody TagDTO tagDTO) {
        return ResponseEntity.ok(tagService.insert(tagDTO));
    }

    @PutMapping("/update-tag")
    @MessageApi("Update a tag name")
    public ResponseEntity<?> update(@RequestBody TagDTO tag) {
        return ResponseEntity.ok().body(tagService.update(tag));
    }

    @GetMapping("/tags")
    @MessageApi("Fetch all tags with pagination")
    public ResponseEntity<PaginationResultDTO> getAllCategories(@Filter Specification<Tag> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.tagService.getAll(spec, pageable));
    }
}
