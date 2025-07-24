package com.HieuPahm.AniHoyo.services.implement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.dtos.CategoryDTO;
import com.HieuPahm.AniHoyo.dtos.FilmDTO;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.TagDTO;
import com.HieuPahm.AniHoyo.entities.Category;
import com.HieuPahm.AniHoyo.entities.Film;
import com.HieuPahm.AniHoyo.repository.CategoryRepository;
import com.HieuPahm.AniHoyo.repository.FilmRepository;
import com.HieuPahm.AniHoyo.repository.TagRepository;
import com.HieuPahm.AniHoyo.services.IFilmService;

@Service
public class FilmService implements IFilmService {
    private final FilmRepository filmRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    public FilmService(FilmRepository filmRepository,ModelMapper modelMapper,
    CategoryRepository categoryRepository, TagRepository tagRepository){
        this.filmRepository = filmRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public FilmDTO insert(FilmDTO dto) {
        if(dto.getCategories() != null){
            List<Long> reqCategory = dto.getCategories().stream().map(item -> item.getId()).collect(Collectors.toList());
            Set<CategoryDTO> mainCategory = this.categoryRepository.findByIdIn(reqCategory);
            dto.setCategories(mainCategory);
        }
        if(dto.getTags() != null){
            List<Long> reqTag = dto.getTags().stream().map(item -> item.getId()).collect(Collectors.toList());
            Set<TagDTO> mainTag = this.tagRepository.findByIdIn(reqTag);
            dto.setTags(mainTag);
        }
        return modelMapper.map(
            filmRepository.save(modelMapper.map(dto, Film.class)), FilmDTO.class);
    }

    @Override
    public FilmDTO getById(Long id) {
        return modelMapper.map(
            filmRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Not Found")), FilmDTO.class);
    }

    @Override
    public void update(FilmDTO dto) {
        filmRepository.save(modelMapper.map(dto, Film.class));
    }

    @Override
    public void delete(Long id) {
       filmRepository.deleteById(id);
    }

    @Override
    public PaginationResultDTO getAll(Specification<Film> spec,Pageable pageable) {
        Page<Film> pageCheck = this.filmRepository.findAll(spec, pageable);
        PaginationResultDTO res = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();
        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setPages(pageCheck.getTotalPages());
        mt.setTotal(pageCheck.getTotalElements());
        res.setMeta(mt);
        //remove sensitive data
        res.setResult(pageCheck.getContent());
        return res;
    }


    @Override
    public FilmDTO getFilmBySeasonId(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFilmBySeasonId'");
    }
    
}
