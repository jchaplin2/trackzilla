package com.trackzilla.controller;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import com.trackzilla.entity.Ticket;
import com.trackzilla.exception.ResourceNotFoundException;
import com.trackzilla.service.ApplicationService;
import com.trackzilla.service.ReleaseService;
import com.trackzilla.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trackzilla")
public class TrackzillaController {
    private final ApplicationService applicationService;
    private final ReleaseService releaseService;
    private final TicketService ticketService;

    public TrackzillaController(ApplicationService applicationService, ReleaseService releaseService, TicketService ticketService) {
        this.applicationService = applicationService;
        this.releaseService = releaseService;
        this.ticketService = ticketService;
    }

    @GetMapping(value = "/applications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.listApplications();

        HttpStatus httpStatus = HttpStatus.OK;
        if(applications.isEmpty()) {
             httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<List<Application>>(applications, httpStatus);
    }

    @GetMapping(value = "/releases", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Release>> getAllReleases() {
        List<Release> releases = releaseService.listReleases();

        HttpStatus httpStatus = HttpStatus.OK;
        if(releases.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<List<Release>>(releases, httpStatus);
    }

    @GetMapping(value = "/application/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> getApplication(@PathVariable("id") long id) {
        Application application = applicationService.findApplication(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR! Application with id: "+ id + " not found."));

        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @GetMapping(value = "/release/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Release> getRelease(@PathVariable("id") long id) {
        Release release = releaseService.findRelease(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR! Release with id: "+ id + " not found."));

        return new ResponseEntity<Release>(release, HttpStatus.OK);
    }

    @GetMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.listTickets();

        HttpStatus httpStatus = HttpStatus.OK;
        if(tickets.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<List<Ticket>>(tickets, httpStatus);
    }

    @GetMapping(value = "/ticket/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> getTicket(@PathVariable("id") long id) {
        Ticket application = ticketService.findTicket(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR! Ticket with id: "+ id + " not found."));

        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @PostMapping(value = "/ticket", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket newTicket) {
        Ticket createdTicket = Optional.of(ticketService.save(newTicket))
                .orElseThrow(()-> new RuntimeException(""));

        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @PostMapping(value = "/release", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Release> addRelease(@RequestBody Release newRelease) {
        Release createdRelease = Optional.of(releaseService.save(newRelease))
                .orElseThrow(()-> new RuntimeException(""));

        return new ResponseEntity<>(createdRelease, HttpStatus.CREATED);
    }

    @PostMapping(value = "/application", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> addApplication(@RequestBody Application newApplication) {
        Application createdApplication = Optional.of(applicationService.save(newApplication))
                .orElseThrow(()-> new RuntimeException(""));

        return new ResponseEntity<>(createdApplication, HttpStatus.CREATED);
    }

}
