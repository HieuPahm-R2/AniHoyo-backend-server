package com.HieuPahm.AniHoyo.services;

import java.util.List;

import com.HieuPahm.AniHoyo.entities.User;

public interface IUserService<T,K> {
    public T create(User data);
    public T getById(K id);
    public List<T> getAll();
    public void delete(K id);
    public void update(T data);

    public boolean CheckEmailExist(String email);
    public User handleGetUserByUsername(String username);
}
