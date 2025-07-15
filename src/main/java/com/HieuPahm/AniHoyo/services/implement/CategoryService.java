package com.HieuPahm.AniHoyo.services.implement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.dtos.CategoryDTO;
import com.HieuPahm.AniHoyo.entities.Category;
import com.HieuPahm.AniHoyo.entities.Film;
import com.HieuPahm.AniHoyo.repository.CategoryRepository;
import com.HieuPahm.AniHoyo.repository.FilmRepository;
import com.HieuPahm.AniHoyo.services.ICategoryService;

@Service
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FilmRepository filmRepository;

    public CategoryService(CategoryRepository categoryRepository,ModelMapper modelMapper, FilmRepository filmRepository){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.filmRepository = filmRepository;
    }

    @Override
    public CategoryDTO insert(CategoryDTO dto) {
        
        return modelMapper.map(categoryRepository.save(modelMapper.map(dto, Category.class)), CategoryDTO.class);
    }

    @Override
    public CategoryDTO getById(Long id) {
        return modelMapper.map(categoryRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException("Not Found")
        ), CategoryDTO.class);
    }

    @Override
    public void update(CategoryDTO dto) {
        categoryRepository.save(modelMapper.map(dto, Category.class));
    }

    
    @Override
    public List<CategoryDTO> getAllById(Set<Long> id) {

        throw new UnsupportedOperationException("Unimplemented method 'getAllById'");
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(null);
        Set<Film> listFilms = category.getFilmList();
        listFilms.forEach(item -> item.getCategories().remove(category));
        filmRepository.saveAll(listFilms);
        categoryRepository.deleteById(id);;
    }

    @Override
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream().map(item -> modelMapper.map(item, CategoryDTO.class)).toList();
    }
    
}
