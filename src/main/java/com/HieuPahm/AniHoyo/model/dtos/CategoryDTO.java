package com.HieuPahm.AniHoyo.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;

    public CategoryDTO(Long id) {
        this.id = id;
    }
}
