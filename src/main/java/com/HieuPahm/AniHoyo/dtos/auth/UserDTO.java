package com.HieuPahm.AniHoyo.dtos.auth;

import java.time.Instant;

import com.HieuPahm.AniHoyo.utils.constant.GenderEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {
    private long id;
    private String fullName;
    private String email;
    private String refreshToken;

    private Instant updatedTime;
    private Instant createdTime;

}
