package com.HieuPahm.AniHoyo.services.implement;

import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.dtos.TagDTO;
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
    public List<TagDTO> getAll() {
        return tagRepository.findAll()
                .stream().map(item -> modelMapper.map(item, TagDTO.class)).toList();
    }

    @Override
    public List<TagDTO> getAllById(Set<Long> id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllById'");
    }
    
}
