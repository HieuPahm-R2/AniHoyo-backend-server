package com.HieuPahm.AniHoyo.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HieuPahm.AniHoyo.dtos.PaginationResultDTO;
import com.HieuPahm.AniHoyo.dtos.auth.UserDTO;
import com.HieuPahm.AniHoyo.entities.User;
import com.HieuPahm.AniHoyo.services.implement.UserService;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
   

    public UserController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
    }

    @PostMapping("/user-create")
    @MessageApi("Create new User action")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User dataUser) throws BadActionException{
        // User accUser = this.userService.handleCreateUser(dataUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.create(dataUser));
    }
    @GetMapping("/user/{id}")
    @MessageApi("Create new User action")
    public ResponseEntity<UserDTO> getInfo(@PathVariable("id") long id) throws BadActionException{
        // User accUser = this.userService.handleCreateUser(dataUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.getInfo(id));
    }
    @DeleteMapping("/user/{id}")
    @MessageApi("Delete acction")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws BadActionException{
        this.userService.delete(id);
        return ResponseEntity.ok(null);
    }
    //get all users
    @GetMapping("/users")
    @MessageApi("Fetch All Users action")
    public ResponseEntity<PaginationResultDTO> getAllUsersInfo(
        @Filter Specification<User> spec, Pageable pageable
    ){
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAll(spec,pageable)) ;
    }
}
