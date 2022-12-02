package com.trackzilla.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="seq", initialValue=6, allocationSize=100)
@Getter
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name="application_id")
    private Long id;

    @JsonProperty("applicationName")
    @Column(name = "app_name", nullable = false)
    private String name;

    @JsonProperty("applicationDesc")
    @Column(length = 2000, name = "description", nullable = false)
    private String description;

    @JsonProperty("applicationOwner")
    private String owner;

    public Application() {
    }

    public Application(String id){
        this.id = Long.parseLong(id);
    }

    public Application(String name, String owner, String description) {
        this.name = name;
        this.owner = owner;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", owner=" + owner +
                ", description='" + description + '\'' +
                '}';
    }
}
