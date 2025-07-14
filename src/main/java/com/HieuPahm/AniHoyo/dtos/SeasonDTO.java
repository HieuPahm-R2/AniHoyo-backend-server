package com.HieuPahm.AniHoyo.dtos;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonDTO {
    private Long id;
    private String SeasonName;
    private Integer releaseYear;
    private Boolean status;
    private Instant uploadDate;
    private List<EpisodeDTO> episodes;
}
