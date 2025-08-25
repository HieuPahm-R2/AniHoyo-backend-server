package com.HieuPahm.AniHoyo.controller;

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

import com.HieuPahm.AniHoyo.model.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.model.dtos.SeasonDTO;
import com.HieuPahm.AniHoyo.model.entities.Category;
import com.HieuPahm.AniHoyo.model.entities.Season;
import com.HieuPahm.AniHoyo.services.implement.SeasonService;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class SeasonController {
    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @PostMapping("/add-season")
    @MessageApi("add a season")
    public ResponseEntity<?> addSeason(@RequestBody SeasonDTO dto) {
        return ResponseEntity.ok().body(this.seasonService.insert(dto));
    }

    @PutMapping("/update-season")
    @MessageApi("update a season")
    public ResponseEntity<?> update(@Valid @RequestBody SeasonDTO dto) throws BadActionException {
        return ResponseEntity.ok().body(this.seasonService.update(dto));
    }

    @GetMapping("/season/{id}")
    @MessageApi("Fetch Season By id")
    public ResponseEntity<?> getSeason(@PathVariable long id) {
        return ResponseEntity.ok().body(this.seasonService.getById(id));
    }

    @DeleteMapping("/delete-season/{id}")
    @MessageApi("delete a season")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        this.seasonService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/seasons")
    public ResponseEntity<PaginationResultDTO> getAllCategories(@Filter Specification<Season> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.seasonService.fetchAll(spec, pageable));
    }

    @GetMapping("/seasons/by-film/{filmId}")
    @MessageApi("fetch all seasons of film")
    public ResponseEntity<PaginationResultDTO> fetchSeasonsByFilm(@PathVariable Long filmId, Pageable pageable) {
        return ResponseEntity.ok().body(this.seasonService.fetchSeasonsByFilm(filmId, pageable));
    }
}
