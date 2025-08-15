package com.HieuPahm.AniHoyo.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.entities.Role;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;

public interface IRoleService {
    Role create(Role data);

    Role update(Role data) throws BadActionException;

    Role fetchById(Long id) throws BadActionException;

    void delete(Long id) throws BadActionException;

    PaginationResultDTO fetchAll(Specification<Role> spec, Pageable pageable);
}
