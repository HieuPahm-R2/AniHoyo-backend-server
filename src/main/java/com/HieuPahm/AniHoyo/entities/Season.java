package com.HieuPahm.AniHoyo.entities;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name ="seasons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private long id;
    private String seasonName;
    private Integer releaseYear;
    private Instant uploadDate;
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    private Film film;

    @OneToMany(mappedBy = "season")
    private List<Episode> episodes;
}
