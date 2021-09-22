package com.trackzilla.controller;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import com.trackzilla.exception.ApplicationNotFoundException;
import com.trackzilla.service.ApplicationService;
import com.trackzilla.service.ReleaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/trackzilla")
public class TrackzillaController {
    ApplicationService applicationService;
    private ReleaseService releaseService;

    TrackzillaController(ApplicationService applicationService, ReleaseService releaseService) {
        this.applicationService = applicationService;
        this.releaseService = releaseService;
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> list = applicationService.listApplications();
        return new ResponseEntity<List<Application>>(list, HttpStatus.OK);
    }

    @GetMapping("/releases")
    public ResponseEntity<List<Release>> getAllReleases() {
        List<Release> list = releaseService.listReleases();
        return new ResponseEntity<List<Release>>(list, HttpStatus.OK);
    }

    @GetMapping("/application/{id}")
    public ResponseEntity<Application> getApplication(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<Application>(applicationService.findApplication(id), HttpStatus.OK);
        } catch (ApplicationNotFoundException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application Not Found");
        }
    }
}
