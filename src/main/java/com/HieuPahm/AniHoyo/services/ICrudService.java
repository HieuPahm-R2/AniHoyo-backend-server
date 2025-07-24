package com.HieuPahm.AniHoyo.services;

import com.HieuPahm.AniHoyo.utils.error.BadActionException;

public interface ICrudService <T,K> {
    public T insert(T dto) throws BadActionException;
    public T getById(K id);
    public void update(T dto);
    public void delete(K id);
    
}
