package com.HieuPahm.AniHoyo.services;

import java.util.List;
import java.util.Set;

public interface ICrudService <T,K> {
    public T insert(T dto);
    public T getById(K id);
    public void update(T dto);
    public void delete(K id);
    public List<T> getAll();
    public List<T> getAllById(Set<K> id);
    
}
