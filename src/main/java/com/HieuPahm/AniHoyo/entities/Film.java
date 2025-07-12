package com.HieuPahm.AniHoyo.entities;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "films")
public class Film {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    @Column(columnDefinition = "nvarchar(150)")
    private String name;
    @Column(columnDefinition = "nvarchar(150)")
    private String studio;
    @Column(columnDefinition = "nvarchar(4000)")
    private String thumbnail;
    @Column(columnDefinition = "nvarchar(4000)")
    private String description;

    private int releaseYear;

    private boolean status;

    private Instant uploadDate;
    private Instant updatedTime;
}
