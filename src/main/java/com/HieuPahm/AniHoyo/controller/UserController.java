package com.HieuPahm.AniHoyo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HieuPahm.AniHoyo.dtos.ResUserDTO;
import com.HieuPahm.AniHoyo.entities.User;
import com.HieuPahm.AniHoyo.services.IUserService;
import com.HieuPahm.AniHoyo.services.implement.UserServiceImpl;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserServiceImpl userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/user/create")
    @MessageApi("Create new User action")
    public ResponseEntity<User> createUser(@Valid @RequestBody User dataUser) throws BadActionException{
        if(this.userService.CheckEmailExist(dataUser.getEmail())){
            throw new BadActionException(
                "Email: " + dataUser.getEmail() + " đã tồn tại, hãy thử email khác"
            );
        }
        String hashPassword = this.passwordEncoder.encode(dataUser.getPassword());
        dataUser.setPassword(hashPassword);
        // User accUser = this.userService.handleCreateUser(dataUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dataUser));
    }
}
