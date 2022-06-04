package com.trackzilla.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="release_seq", initialValue=5, allocationSize=100)
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "release_seq")
    private Long id;

    @JsonProperty("releaseDate")
    @Column(name = "release_date", nullable = false)
    private String date;

    @JsonProperty("releaseDesc")
    private String description;

    public Release() {
    }

    public Release(String description, String releaseDate){
        this.date = releaseDate;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String releaseDate) {
        this.date = releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
