package com.HieuPahm.AniHoyo.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TagDTO {
    private Long id;
    private String tagName;

    public TagDTO(Long id) {
        this.id = id;
    }
}
