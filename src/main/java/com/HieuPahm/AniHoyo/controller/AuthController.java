package com.HieuPahm.AniHoyo.controller;

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

import com.HieuPahm.AniHoyo.dtos.LoginDTO;
import com.HieuPahm.AniHoyo.dtos.ResLoginDTO;
import com.HieuPahm.AniHoyo.services.IUserService;
import com.HieuPahm.AniHoyo.utils.SecurityUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
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
        String access_token = this.securityUtils.generateAccessToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccessToken(access_token);
        return ResponseEntity.ok().body(resLoginDTO);
    }
}
