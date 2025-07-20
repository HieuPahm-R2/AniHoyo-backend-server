package com.HieuPahm.AniHoyo.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "episodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Episode {
    @Id
    private String id;
    @Column(columnDefinition = "nvarchar(150)")
    private String title;

    private String filePath;
    private String contentType;

    private Instant createdTime;
    private Instant updatedTime;

    private boolean status;
    
    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;
}
