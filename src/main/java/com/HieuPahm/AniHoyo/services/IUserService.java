package com.HieuPahm.AniHoyo.services;


import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.auth.UserDTO;
import com.HieuPahm.AniHoyo.entities.User;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;

public interface IUserService {
    public UserDTO create(User data) throws BadActionException;
    public void getById(Long id);
    public PaginationResultDTO getAll();
    public void delete(User id);
    public void update(User data);

    public boolean CheckEmailExist(String email);
    public User handleGetUserByUsername(String username);
}
