package com.HieuPahm.AniHoyo.services.implement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.dtos.CategoryDTO;
import com.HieuPahm.AniHoyo.dtos.FilmDTO;
import com.HieuPahm.AniHoyo.entities.Category;
import com.HieuPahm.AniHoyo.entities.Film;
import com.HieuPahm.AniHoyo.repository.CategoryRepository;
import com.HieuPahm.AniHoyo.repository.FilmRepository;
import com.HieuPahm.AniHoyo.services.IFilmService;

@Service
public class FilmService implements IFilmService {
    private final FilmRepository filmRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;
    public FilmService(FilmRepository filmRepository,ModelMapper modelMapper,
    CategoryRepository categoryRepository){
        this.filmRepository = filmRepository;
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public FilmDTO insert(FilmDTO dto) {
        if(dto.getCategories() != null){
            List<Long> reqCategory = dto.getCategories().stream().map(item -> item.getId()).collect(Collectors.toList());
            Set<CategoryDTO> mainCategory = this.categoryRepository.findByIdIn(reqCategory);
            dto.setCategories(mainCategory);
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
    public List<FilmDTO> getAll() {
        return filmRepository.findAll()
                .stream().map(item -> modelMapper.map(item, FilmDTO.class)).toList();
    }

    @Override
    public List<FilmDTO> getAllById(Set<Long> id) {
        
        throw new UnsupportedOperationException("Unimplemented method 'getAllById'");
    }

    @Override
    public List<FilmDTO> getAllByCategoryId(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'getAllByCategoryId'");
    }

    @Override
    public FilmDTO getFilmBySeasonId(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFilmBySeasonId'");
    }

    
}
