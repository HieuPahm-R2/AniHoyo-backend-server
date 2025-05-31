package com.HieuPahm.AniHoyo.config;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import org.springframework.security.core.userdetails.User;
import com.HieuPahm.AniHoyo.services.UserService;

@Component("userDetailsService")
public class UserDetailsConfig implements UserDetailsService{
    private final UserService userService;
    public UserDetailsConfig(UserService userService){
        this.userService = userService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.HieuPahm.AniHoyo.entities.User user = this.userService.handleGetUserByUsername(username);
        return new User(user.getEmail(), user.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE-USER")));
    }
}
