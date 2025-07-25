package com.HieuPahm.AniHoyo.services.implement;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.auth.UpdateUserDTO;
import com.HieuPahm.AniHoyo.dtos.auth.UserDTO;
import com.HieuPahm.AniHoyo.entities.User;
import com.HieuPahm.AniHoyo.repository.UserRepository;
import com.HieuPahm.AniHoyo.services.IUserService;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;

@Service
public class UserService implements IUserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder
        ,ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }
    

    @Override
    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }


    @Override
    public UserDTO create(User data) throws BadActionException {
        if(this.userRepository.existsByEmail(data.getEmail())){
            throw new BadActionException("Email đã được sử dụng, hãy thử email khác!");
        }
        String hashPassword = this.passwordEncoder.encode(data.getPassword());
        data.setPassword(hashPassword);
        this.userRepository.save(data);
        return this.modelMapper.map(data, UserDTO.class);
    }

     @Override
    public UserDTO getInfo(Long id) {
        return modelMapper.map(this.userRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException("Not Found!")
        ),UserDTO.class);
    }
    public UserDTO convertToUserDTO(User user){
        UserDTO res = new UserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setFullName(user.getFullName());
        res.setCreatedTime(user.getCreatedTime());
        return res;
    }
    @Override
    public PaginationResultDTO getAll(Specification<User> spec,Pageable pageable) {
        Page<User> pageCheck = this.userRepository.findAll(spec, pageable);
        PaginationResultDTO res = new PaginationResultDTO();
        PaginationResultDTO.Meta mt = new PaginationResultDTO.Meta();
        mt.setPage(pageCheck.getNumber() + 1);
        mt.setPageSize(pageCheck.getSize());
        mt.setPages(pageCheck.getTotalPages());
        mt.setTotal(pageCheck.getTotalElements());
        res.setMeta(mt);
        //remove sensitive data
        List<UserDTO> listUser = pageCheck.getContent()
                .stream().map(item -> this.convertToUserDTO(item))
                .collect(Collectors.toList());
        res.setResult(listUser);
        return res;
    }


    @Override
    public void delete(Long id) throws BadActionException {
        Optional<User> currentUser = this.userRepository.findById(id);
        if(!currentUser.isPresent()){
           throw new BadActionException("Not Found");
        }
        this.userRepository.deleteById(id);
    }


    @Override
    public UpdateUserDTO update(User data) throws BadActionException {
        Optional<User> currentUser = this.userRepository.findById(data.getId());
        if(!currentUser.isPresent()){
           throw new BadActionException("Not Found");
        }
        currentUser.get().setFullName(data.getFullName());
        return modelMapper.map(currentUser.get(), UpdateUserDTO.class);
    }
    
}
