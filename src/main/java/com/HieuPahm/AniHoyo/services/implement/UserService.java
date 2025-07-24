package com.HieuPahm.AniHoyo.services.implement;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
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
    public void getById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }


    @Override
    public PaginationResultDTO getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }


    @Override
    public void delete(User id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }


    @Override
    public void update(User data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }


    @Override
    public boolean CheckEmailExist(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'CheckEmailExist'");
    }

   

    
}
