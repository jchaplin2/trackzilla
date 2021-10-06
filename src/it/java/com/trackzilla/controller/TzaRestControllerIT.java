package com.trackzilla.controller;

import com.trackzilla.TrackzillaApplication;
import com.trackzilla.config.DbConfig;
import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import com.trackzilla.entity.Ticket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(classes = {TrackzillaApplication.class, DbConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("it")
public class TzaRestControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getAllApplications() throws Exception {
        ResponseEntity<List> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/trackzilla/applications/", List.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getAllTickets() throws Exception {
        ResponseEntity<List> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/trackzilla/tickets/", List.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getAllReleases() throws Exception {
        ResponseEntity<List> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/trackzilla/releases/", List.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneApplication() throws Exception {
        ResponseEntity<Application> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/trackzilla/application/1", Application.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneTicket() throws Exception {
        ResponseEntity<Ticket> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/trackzilla/ticket/1", Ticket.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneRelease() throws Exception {
        ResponseEntity<Release> response =
                this.restTemplate.getForEntity("http://localhost:" + port + "/trackzilla/ticket/1", Release.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

}
