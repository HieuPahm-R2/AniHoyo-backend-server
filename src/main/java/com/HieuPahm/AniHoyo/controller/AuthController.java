package com.HieuPahm.AniHoyo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HieuPahm.AniHoyo.dtos.auth.LoginDTO;
import com.HieuPahm.AniHoyo.dtos.auth.ResLoginDTO;
import com.HieuPahm.AniHoyo.dtos.auth.UserDTO;
import com.HieuPahm.AniHoyo.entities.User;
import com.HieuPahm.AniHoyo.services.IUserService;
import com.HieuPahm.AniHoyo.utils.SecurityUtils;
import com.HieuPahm.AniHoyo.utils.anotation.MessageApi;
import com.HieuPahm.AniHoyo.utils.error.BadActionException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${anihoyo.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpire;
    
    private final IUserService userService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;
    
    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
                IUserService userService,PasswordEncoder passwordEncoder,SecurityUtils securityUtils){
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.securityUtils = securityUtils;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginData){
        // transfer input include username/password
          UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(
            loginData.getUsername(), loginData.getPassword());
        // xác thực người dùng ==> cần có loadUserByUserName
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User realUser = this.userService.handleGetUserByUsername(loginData.getUsername());
        if(realUser != null){
            ResLoginDTO.UserData userLog = new ResLoginDTO.UserData(
                realUser.getId(),
                realUser.getEmail(),
                realUser.getFullName()
            );
            resLoginDTO.setUser(userLog);
        }
        // generate access token
        String access_token = this.securityUtils.generateAccessToken(authentication);
        resLoginDTO.setAccessToken(access_token);
        // gen refresh token
        String refresh_token = this.securityUtils.generateRefreshToken(loginData.getUsername(), resLoginDTO);
        this.userService.saveRefreshToken(access_token, refresh_token);
         //setup cookies
        ResponseCookie resCookies = ResponseCookie
                    .from("refresh-token", refresh_token)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(refreshTokenExpire)
                    .build();
        return ResponseEntity.ok().body(resLoginDTO);
    }
    @PostMapping("/auth/register")
    @MessageApi("Register account")
    public ResponseEntity<UserDTO> registerAccount(@Valid @RequestBody User dataUser) throws BadActionException{
        // User accUser = this.userService.handleCreateUser(dataUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.create(dataUser));
    }
}
