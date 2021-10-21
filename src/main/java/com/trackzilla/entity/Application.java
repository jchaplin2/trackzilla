package com.trackzilla.entity;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="seq", initialValue=6, allocationSize=100)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name="application_id")
    private Long id;

    @Column(name = "app_name", nullable = false)
    private String name;

    @Column(length = 2000, name = "description", nullable = false)
    private String description;

    private String owner;

    public Application() {
    }

    public Application(String name, String owner, String description) {
        this.name = name;
        this.owner = owner;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

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
