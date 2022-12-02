package com.trackzilla.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Getter
@Setter
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
}
