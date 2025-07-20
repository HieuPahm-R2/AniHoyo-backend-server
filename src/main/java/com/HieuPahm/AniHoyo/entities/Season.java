package com.HieuPahm.AniHoyo.entities;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    private String thumb;
    private Integer releaseYear;
    private Instant uploadDate;
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @OneToMany(mappedBy = "season", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Episode> episodes;
}
