package com.HieuPahm.AniHoyo.dtos;

import java.time.Instant;
import java.util.List;

import com.HieuPahm.AniHoyo.entities.Episode;
import com.HieuPahm.AniHoyo.entities.Film;
import com.HieuPahm.AniHoyo.utils.constant.GenersEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonDTO {
    private Long id;
    private String seasonName;
    private String thumb;
    private Integer releaseYear;
    private Instant uploadDate;
    private GenersEnum type;
    private Film film;
    private List<Episode> episodes;
}
