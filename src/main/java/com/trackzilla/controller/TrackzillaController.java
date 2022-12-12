package com.trackzilla.controller;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import com.trackzilla.entity.Ticket;
import com.trackzilla.exception.ResourceNotFoundException;
import com.trackzilla.service.ApplicationService;
import com.trackzilla.service.ReleaseService;
import com.trackzilla.service.TicketService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(TrackzillaController.BASE_ENDPOINT)
public class TrackzillaController {
    private final ApplicationService applicationService;
    private final ReleaseService releaseService;
    private final TicketService ticketService;

    public final static String BASE_ENDPOINT = "/trackzilla";

    public final static String APPLICATIONS_ENDPOINT = "/applications";
    public final static String APPLICATIONS_BY_DESC_ENDPOINT = "/applicationsByDesc";
    public final static String APPLICATIONS_ID_ENDPOINT = "/applications/{id}";

    public final static String RELEASES_ENDPOINT = "/releases";
    public final static String RELEASES_BY_DESC_ENDPOINT = "/releasesByDesc";
    public final static String RELEASES_ID_ENDPOINT = "/releases/{id}";

    public final static String TICKETS_ENDPOINT = "/tickets";
    public final static String TICKETS_ID_ENDPOINT = "/tickets/{id}";

    public TrackzillaController(ApplicationService applicationService, ReleaseService releaseService, TicketService ticketService) {
        this.applicationService = applicationService;
        this.releaseService = releaseService;
        this.ticketService = ticketService;
    }

    @GetMapping(value = APPLICATIONS_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationService.listApplications();

        HttpStatus httpStatus = HttpStatus.OK;
        if(applications.isEmpty()) {
             httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<List<Application>>(applications, httpStatus);
    }

    @GetMapping(value = RELEASES_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Release>> getAllReleases() {
        List<Release> releases = releaseService.listReleases();

        HttpStatus httpStatus = HttpStatus.OK;
        if(releases.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<List<Release>>(releases, httpStatus);
    }

    @GetMapping(value = RELEASES_BY_DESC_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Release>> getReleasesByDesc(@RequestParam(value="desc", required = true) String desc) {
        List<Release> releases = releaseService.findByDescription(desc).orElse(new ArrayList<Release>());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return new ResponseEntity<List<Release>>(releases, headers ,HttpStatus.OK);
    }

    @GetMapping(value = APPLICATIONS_BY_DESC_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Application>> getApplicationsByDesc(@RequestParam(value="desc", required = true) String desc) {
        List<Application> releases = applicationService.findByDescription(desc).orElse(new ArrayList<Application>());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return new ResponseEntity<List<Application>>(releases, headers ,HttpStatus.OK);
    }

    @GetMapping(value = APPLICATIONS_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> getApplication(@PathVariable("id") long id) {
        Application application = applicationService.findApplication(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR! Application with id: "+ id + " not found."));

        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @GetMapping(value = RELEASES_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Release> getRelease(@PathVariable("id") long id) {
        Release release = releaseService.findRelease(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR! Release with id: "+ id + " not found."));

        return new ResponseEntity<Release>(release, HttpStatus.OK);
    }

    @GetMapping(value = TICKETS_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ticket>> getAllTickets() {
        List<Ticket> tickets = ticketService.listTickets();

        HttpStatus httpStatus = HttpStatus.OK;
        if(tickets.isEmpty()) {
            httpStatus = HttpStatus.NO_CONTENT;
        }

        return new ResponseEntity<List<Ticket>>(tickets, httpStatus);
    }

    @GetMapping(value = TICKETS_ID_ENDPOINT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> getTicket(@PathVariable("id") long id) {
        Ticket ticket = ticketService.findTicket(id)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR! Ticket with id: "+ id + " not found."));

        return new ResponseEntity<>(ticket, HttpStatus.OK);
    }

    @PostMapping(value = TICKETS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> addTicket(@RequestBody Ticket newTicket) {
        Ticket createdTicket = Optional.of(ticketService.save(newTicket))
                .orElseThrow(()-> new RuntimeException("ERROR! An error occurred while adding ticket."));

        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @PostMapping(value = RELEASES_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Release> addRelease(@RequestBody Release newRelease) {
        Release createdRelease = Optional.of(releaseService.save(newRelease))
                .orElseThrow(()-> new RuntimeException("ERROR! An error occurred while adding release."));

        return new ResponseEntity<>(createdRelease, HttpStatus.CREATED);
    }

    @PostMapping(value = APPLICATIONS_ENDPOINT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> addApplication(@RequestBody Application newApplication) {
        Application createdApplication = Optional.of(applicationService.save(newApplication))
                .orElseThrow(()-> new RuntimeException("ERROR! An error occurred while adding application."));

        return new ResponseEntity<>(createdApplication, HttpStatus.CREATED);
    }

    @PutMapping(value = TICKETS_ID_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> updateTicket(@PathVariable(value = "id") Long ticketId,
            @RequestBody Ticket updatedTicket) throws ResourceNotFoundException {

        Ticket ticket = ticketService.findTicket(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR!! Ticket not found for this id :: " + ticketId));

        ticket.setStatus(updatedTicket.getStatus());
        ticket.setDescription(updatedTicket.getDescription());
        ticket.setTitle(updatedTicket.getTitle());

        Ticket savedTicket = Optional.of(ticketService.save(ticket))
                .orElseThrow(()-> new RuntimeException("ERROR! An error occurred while updating ticket id "+ticketId));

        return new ResponseEntity<>(savedTicket, HttpStatus.OK);
    }

    @PutMapping(value = RELEASES_ID_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Release> updateRelease(@PathVariable(value = "id") Long releaseId,
            @RequestBody Release updatedRelease) throws ResourceNotFoundException {

        Release release = releaseService.findRelease(releaseId)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR!! Release not found for this id :: " + releaseId));

        release.setDate(updatedRelease.getDate());
        release.setDescription(updatedRelease.getDescription());

        Release saveddRelease = Optional.of(releaseService.save(release))
                .orElseThrow(()-> new RuntimeException("ERROR! An error occurred while updating release id "+releaseId));

        return new ResponseEntity<>(saveddRelease, HttpStatus.OK);
    }

    @PutMapping(value = APPLICATIONS_ID_ENDPOINT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Application> updateApplication(@PathVariable(value = "id") Long applicationId,
                                               @RequestBody Application updatedApplication) throws ResourceNotFoundException {

        Application application = applicationService.findApplication(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("ERROR!! Application not found for this id :: " + applicationId));

        application.setDescription(updatedApplication.getDescription());
        application.setOwner(updatedApplication.getOwner());
        application.setName(updatedApplication.getName());

        Application savedApplication = Optional.of(applicationService.save(application))
                .orElseThrow(()-> new RuntimeException("ERROR! An error occurred while updating app id "+applicationId));

        return new ResponseEntity<>(savedApplication, HttpStatus.OK);
    }

}
