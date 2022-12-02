package com.trackzilla.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
}
