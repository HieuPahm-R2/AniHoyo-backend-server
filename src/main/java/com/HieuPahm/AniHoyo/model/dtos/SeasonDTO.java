package com.HieuPahm.AniHoyo.model.dtos;

import java.time.Instant;
import java.util.List;

import com.HieuPahm.AniHoyo.model.entities.Episode;
import com.HieuPahm.AniHoyo.model.entities.Film;
import com.HieuPahm.AniHoyo.utils.constant.GenersEnum;
import com.HieuPahm.AniHoyo.utils.constant.StatusEnum;

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
    private String description;
    private Integer releaseYear;
    private Instant uploadDate;
    private GenersEnum type;
    private StatusEnum status;
    private String trailer;
    private Long viewCount;
    private Film film;
    private List<Episode> episodes;
}
