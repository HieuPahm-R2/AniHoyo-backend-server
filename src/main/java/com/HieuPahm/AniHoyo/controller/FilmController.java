package com.HieuPahm.AniHoyo.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HieuPahm.AniHoyo.dtos.FilmDTO;

import com.HieuPahm.AniHoyo.repository.FilmRepository;
import com.HieuPahm.AniHoyo.services.implement.FilmService;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class FilmController {
    private final FilmService filmService;
    private final FilmRepository filmRepository;
    public FilmController(FilmService filmService, FilmRepository filmRepository){
        this.filmService = filmService;
        this.filmRepository = filmRepository;
    }
    @PostMapping("/add-film")
    @MessageApi("Add new film")
    public ResponseEntity<?> addFilm(@RequestBody FilmDTO filmDTO ){
        return ResponseEntity.ok(filmService.insert(filmDTO));
    }
     // # update task
    @PutMapping("/update-film")
    @MessageApi("Edit film info action")
    public ResponseEntity<?> updateJob(@Valid @RequestBody FilmDTO filmDTO) throws BadActionException{
        this.filmService.update(filmDTO);
        return ResponseEntity.ok("update done");
    }
   @GetMapping("/get-all-films")
   @MessageApi("Fetch all data films")
   public ResponseEntity<?> getAllFilms(){
    ArrayList<FilmDTO> filmsDTO = new ArrayList<>(filmService.getAll());
    Collections.reverse(filmsDTO);
    return ResponseEntity.ok(filmsDTO);
   }
}
