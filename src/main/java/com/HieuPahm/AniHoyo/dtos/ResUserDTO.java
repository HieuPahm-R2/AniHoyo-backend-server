package com.HieuPahm.AniHoyo.dtos;

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
public class ResUserDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;

    private Instant updatedTime;
    private Instant createdTime;
}
