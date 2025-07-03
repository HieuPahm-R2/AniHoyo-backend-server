package com.HieuPahm.AniHoyo.entities;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String title;
    private String description;

    private String filePath;
    private String contentType;

    private Instant createdTime;
    private Instant updatedTime;

    private boolean status;
}
