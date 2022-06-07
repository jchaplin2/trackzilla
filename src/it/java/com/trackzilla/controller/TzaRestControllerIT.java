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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.trackzilla.controller.TrackzillaController.*;
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

    public static final String ROOT_URL = "http://localhost:";

    //NOTE: based on insert values set up in data-it.sql
    public static final int NUMBER_OF_EXISTING_APPLICATIONS = 5;
    public static final int NUMBER_OF_EXISTING_TICKETS = 4;
    public static final int NUMBER_OF_EXISTING_RELEASES = 4;

    @Test
    public void getAllApplications() throws Exception {
        ResponseEntity<List> response =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + APPLICATIONS_ENDPOINT, List.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getAllTickets() throws Exception {
        ResponseEntity<List> response =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + TICKETS_ENDPOINT, List.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getAllReleases() throws Exception {
        ResponseEntity<List> response =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + RELEASES_ENDPOINT, List.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneApplication() throws Exception {
        ResponseEntity<Application> response =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + APPLICATIONS_ENDPOINT + "/1", Application.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneTicket() throws Exception {
        ResponseEntity<Ticket> response =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + TICKETS_ENDPOINT + "/1", Ticket.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneRelease() throws Exception {
        ResponseEntity<Release> response =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + TICKETS_ENDPOINT + "/1", Release.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void postApplication() throws Exception {
        String url = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + APPLICATIONS_ENDPOINT);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", "app1");
        params.put("description", "first app");
        params.put("owner", "me");

        HttpEntity<HashMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());
        this.restTemplate.postForEntity(url, request, Application.class);

        ResponseEntity<ArrayList> response =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + APPLICATIONS_ENDPOINT, ArrayList.class);

        int numberOfExpectedApps = NUMBER_OF_EXISTING_APPLICATIONS + 1;
        assertThat(response.getBody().size(), equalTo( Integer.valueOf(numberOfExpectedApps) ));
    }

    @Test
    public void postRelease() throws Exception {
        String url = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + RELEASES_ENDPOINT);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("description", "first release");
        params.put("releaseDate", "01/01/1900");

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(params, new HttpHeaders());
        this.restTemplate.postForEntity(url, request, Release.class);

        String getUrl = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + RELEASES_ENDPOINT);
        ResponseEntity<ArrayList> response =
                this.restTemplate.getForEntity(getUrl, ArrayList.class);

        int numberOfExpectedReleases = NUMBER_OF_EXISTING_RELEASES + 1;
        assertThat(response.getBody().size(), equalTo( Integer.valueOf(numberOfExpectedReleases) ));
    }

    @Test
    public void postTicket() throws Exception {
        ResponseEntity<Application> appResponse =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + APPLICATIONS_ENDPOINT + "/1", Application.class);
        Application app = appResponse.getBody();

        ResponseEntity<Release> releaseResponse =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + RELEASES_ENDPOINT + "/1", Release.class);
        Release rel = releaseResponse.getBody();

        String url = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + TICKETS_ENDPOINT);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("title", "My new feature");
        params.put("description", "best description");
        params.put("application", app);
        params.put("release", rel);
        params.put("status", "OPEN");

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(params, new HttpHeaders());
        this.restTemplate.postForEntity(url, request, Ticket.class);

        String getUrl = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + TICKETS_ENDPOINT);
        ResponseEntity<ArrayList> response =
                this.restTemplate.getForEntity(getUrl, ArrayList.class);

        int numberOfExpectedTickets = NUMBER_OF_EXISTING_TICKETS + 1;
        assertThat(response.getBody().size(), equalTo( Integer.valueOf(numberOfExpectedTickets) ));
    }

    @Test
    public void updateApplication() throws Exception {
        String url = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + APPLICATIONS_ENDPOINT + "/1");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(1));
        params.put("name", "app1");
        params.put("description", "first app");
        params.put("owner", "me");

        HttpEntity<HashMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());
        this.restTemplate.put(url, request, Application.class);

        ResponseEntity<Application> response =
                this.restTemplate.getForEntity(ROOT_URL + port + BASE_ENDPOINT + APPLICATIONS_ENDPOINT + "/1", Application.class);

        assertThat(response.getBody().getName(), equalTo( "app1" ));
        assertThat(response.getBody().getDescription(), equalTo( "first app" ));
        assertThat(response.getBody().getOwner(), equalTo( "me" ));
    }

    @Test
    public void updateRelease() throws Exception {
        String url = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + RELEASES_ENDPOINT + "/1");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("releaseDesc", "first release");
        params.put("releaseDate", "01/01/1900");

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(params, new HttpHeaders());
        this.restTemplate.put(url, request, Release.class);

        String getUrl = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + RELEASES_ENDPOINT + "/1");
        ResponseEntity<Release> response =
                this.restTemplate.getForEntity(getUrl, Release.class);

        assertThat(response.getBody().getDescription(), equalTo( "first release" ));
        assertThat(response.getBody().getDate(), equalTo( "01/01/1900" ));
    }

    @Test
    public void updateTicket() throws Exception {
        String url = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + TICKETS_ENDPOINT + "/1");

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("title", "My Title");
        params.put("description", "My Description");
        params.put("status", "OPEN");

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(params, new HttpHeaders());
        this.restTemplate.put(url, request, Ticket.class);

        String getUrl = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + TICKETS_ENDPOINT + "/1");
        ResponseEntity<Ticket> response =
                this.restTemplate.getForEntity(getUrl, Ticket.class);

        assertThat(response.getBody().getTitle(), equalTo( "My Title" ));
        assertThat(response.getBody().getDescription(), equalTo( "My Description" ));
        assertThat(response.getBody().getStatus(), equalTo( "OPEN" ));
    }

}
