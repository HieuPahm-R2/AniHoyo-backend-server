package com.HieuPahm.AniHoyo.utils.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.HieuPahm.AniHoyo.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {
     @ExceptionHandler(value = {
        UsernameNotFoundException.class,
        BadCredentialsException.class,
        BadActionException.class
    })
    public ResponseEntity<RestResponse<Object>> handleBadActionException(Exception ex){
        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError(ex.getMessage());
        res.setMessage("INVALID EXCEPTION OCCURS...");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
