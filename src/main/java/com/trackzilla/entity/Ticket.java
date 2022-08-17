package com.trackzilla.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@SequenceGenerator(name="ticket_seq", initialValue=5, allocationSize=100)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_seq")
    private Long id;

    @JsonProperty("ticketTitle")
    private String title;

    @JsonProperty("ticketDesc")
    private String description;

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonProperty("applicationId")
    private Application application;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    @JoinTable(name ="ticket_release", joinColumns = @JoinColumn(name = "ticket_fk"),
            inverseJoinColumns = @JoinColumn(name = "release_fk"))
    private Release release;

    @JsonProperty("ticketStatus")
    private String status;

    public Ticket() {
    }

    public Ticket(String title, String description,
                  Application application, Release release, String status) {
        this.title = title;
        this.description = description;
        this.application = application;
        this.release = release;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }
}
