package com.HieuPahm.AniHoyo.model.dtos;

import com.HieuPahm.AniHoyo.model.entities.Season;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {
    private Long id;

    private String title;

    private String filePath;
    private String contentType;

    private Season season;
}
