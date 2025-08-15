package com.HieuPahm.AniHoyo.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.auth.UpdateUserDTO;
import com.HieuPahm.AniHoyo.dtos.auth.UserDTO;
import com.HieuPahm.AniHoyo.entities.User;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;

public interface IUserService {
    public UserDTO create(User data) throws BadActionException;

    public UserDTO getInfo(Long id);

    public PaginationResultDTO getAll(Specification<User> spec, Pageable pageable);

    public void delete(Long id) throws BadActionException;

    public UpdateUserDTO update(User data) throws BadActionException;

    public User fetchWithTokenAndEmail(String token, String email);

    public void saveRefreshToken(String token, String email);

    public User handleGetUserByUsername(String username);
}
