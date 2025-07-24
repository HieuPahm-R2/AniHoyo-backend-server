package com.HieuPahm.AniHoyo.services.implement;

import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.TagDTO;
import com.HieuPahm.AniHoyo.entities.Film;
import com.HieuPahm.AniHoyo.entities.Tag;
import com.HieuPahm.AniHoyo.repository.TagRepository;
import com.HieuPahm.AniHoyo.services.ITagService;

@Service
public class TagService implements ITagService {
    
    private final ModelMapper modelMapper;
    private final TagRepository tagRepository;
    public TagService(ModelMapper modelMapper, TagRepository tagRepository){
        this.tagRepository = tagRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TagDTO insert(TagDTO dto) {
       return modelMapper.map(tagRepository.save(modelMapper.map(dto, Tag.class)), TagDTO.class);
    }

    @Override
    public TagDTO getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }

    @Override
    public void update(TagDTO dto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public PaginationResultDTO getAll(Specification<Tag> spec,Pageable pageable) {
        Page<Tag> pageCheck = this.tagRepository.findAll(spec, pageable);
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

    
}
