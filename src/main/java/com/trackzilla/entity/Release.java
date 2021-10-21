package com.trackzilla.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="release_seq", initialValue=5, allocationSize=100)
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "release_seq")
    private Long id;

    @Column(name = "release_date", nullable = false)
    private String releaseDate;
    private String description;

    public Release() {
    }

    public Release(String description, String releaseDate){
        this.releaseDate = releaseDate;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
