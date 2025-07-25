package com.HieuPahm.AniHoyo.dtos.auth;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    private long id;
    private String name;
    private String email;
    private String refreshToken;

    private Instant updatedTime;
}
