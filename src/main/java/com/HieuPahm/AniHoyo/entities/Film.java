package com.HieuPahm.AniHoyo.entities;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    private String slider;
    @Column(columnDefinition = "nvarchar(4000)")
    private String description;

    private int releaseYear;

    private boolean status;

    private Instant uploadDate;
    private Instant updatedTime;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"films"})
    @JoinTable(name = "film_category", joinColumns = @JoinColumn(name = "film_id"), 
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();
    
    @OneToMany(mappedBy = "film", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Season> seasons;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"films"})
    @JoinTable(name = "film_tag", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;
}
