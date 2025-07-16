package com.HieuPahm.AniHoyo.controller;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HieuPahm.AniHoyo.dtos.CategoryDTO;
import com.HieuPahm.AniHoyo.dtos.TagDTO;
import com.HieuPahm.AniHoyo.services.implement.TagService;

@RestController
@RequestMapping("/api/v1")
public class TagController {
    private final TagService tagService;
    public TagController(TagService tagService){
        this.tagService = tagService;
    }
    @PostMapping("/add-filmtag")
    public ResponseEntity<?> addCategory(@RequestBody TagDTO tagDTO){
        return ResponseEntity.ok(tagService.insert(tagDTO));
    }
    @GetMapping("/get-all-tags")
    public ResponseEntity<?> getAllCategories(){
        ArrayList<TagDTO> tagsDTO = new ArrayList<>(tagService.getAll());
        Collections.reverse(tagsDTO);
        return ResponseEntity.ok(tagsDTO);
    }
}
