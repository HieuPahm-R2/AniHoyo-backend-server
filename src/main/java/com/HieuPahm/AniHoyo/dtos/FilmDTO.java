package com.HieuPahm.AniHoyo.dtos;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmDTO {
    private long id;
    
    private String name;
   
    private String studio;
   
    private String thumbnail;
    
    private String description;

    private int releaseYear;

    private boolean status;

    private Set<CategoryDTO> categories;
    private List<SeasonDTO> seasons;
    private Set<TagDTO> tags;
    
    private Instant uploadDate;
}
