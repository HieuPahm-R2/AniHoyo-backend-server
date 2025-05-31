package com.HieuPahm.AniHoyo.services.implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.HieuPahm.AniHoyo.entities.User;
import com.HieuPahm.AniHoyo.repository.UserRepository;
import com.HieuPahm.AniHoyo.services.UserService;

@Service
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public boolean CheckEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }
    @Override
    public User create(User user) {
        return this.userRepository.save(user);
    }
    @Override
    public Object getById(Object id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getById'");
    }
    @Override
    public List getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }
    @Override
    public void delete(Object id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    @Override
    public void update(Object data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }
}
