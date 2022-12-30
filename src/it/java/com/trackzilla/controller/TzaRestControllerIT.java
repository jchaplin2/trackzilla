package com.trackzilla.controller;

import com.trackzilla.TrackzillaApplication;
import com.trackzilla.config.DbConfig;
import com.trackzilla.config.WebSecurityITConfig;
import com.trackzilla.entity.Application;
import com.trackzilla.entity.JwtResponse;
import com.trackzilla.entity.Release;
import com.trackzilla.entity.Ticket;
import com.trackzilla.security.AuthTokenFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.trackzilla.controller.TrackzillaController.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {TrackzillaApplication.class, DbConfig.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(value = WebSecurityITConfig.class)
@ActiveProfiles("it")
public class TzaRestControllerIT {
    @LocalServerPort
    private int port;

    protected TestRestTemplate restTemplate;

    public static final String ROOT_URL = "http://localhost:";

    public static final String BEARER = "Bearer ";

    //NOTE: based on insert values set up in data-it.sql
    public static final int NUMBER_OF_EXISTING_APPLICATIONS = 5;
    public static final int NUMBER_OF_EXISTING_TICKETS = 4;
    public static final int NUMBER_OF_EXISTING_RELEASES = 4;

    private String token;

    @PostConstruct
    public void postConstruct(){
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().rootUri(ROOT_URL + port + "/");
        restTemplate = new TestRestTemplate(restTemplateBuilder);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", "admin");
        params.put("password", "123456");
        HttpEntity<HashMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());
        ResponseEntity<JwtResponse> response = restTemplate.postForEntity("/api/auth/signin", request, JwtResponse.class);

        token = response.getBody().getAccessToken();
    }

    @Test
    public void getAllApplications() throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add(AuthTokenFilter.AUTHORIZATION, BEARER + token);

        ResponseEntity<List> response =
                this.restTemplate.exchange(
                        BASE_APPLICATIONS_ENDPOINT, HttpMethod.GET, new HttpEntity<Object>(headers), List.class, headers
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getAllApplicationsException() throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        final String bad_token = "abc123";
        headers.add(AuthTokenFilter.AUTHORIZATION, BEARER + bad_token);

        ResponseEntity<List> response =
                this.restTemplate.exchange(
                        BASE_APPLICATIONS_ENDPOINT, HttpMethod.GET, new HttpEntity<Object>(headers), List.class, headers
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    public void getAllTickets() throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        ResponseEntity<List> response =
                this.restTemplate.exchange(
                        BASE_TICKETS_ENDPOINT, HttpMethod.GET, new HttpEntity<Object>(headers), List.class, headers
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getAllReleases() throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        ResponseEntity<List> response =
                this.restTemplate.exchange(
                        BASE_RELEASES_ENDPOINT, HttpMethod.GET, new HttpEntity<Object>(headers), List.class, headers
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneApplication() throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        ResponseEntity<Application> response =
                this.restTemplate.exchange(
                        BASE_APPLICATIONS_ENDPOINT + "/1", HttpMethod.GET, new HttpEntity<Object>(headers), Application.class, headers
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneTicket() throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        ResponseEntity<Ticket> response =
                this.restTemplate.exchange(
                        BASE_TICKETS_ENDPOINT + "/1", HttpMethod.GET, new HttpEntity<Object>(headers), Ticket.class, headers
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void getOneRelease() throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        ResponseEntity<Release> response =
                this.restTemplate.exchange(
                        BASE_ENDPOINT + RELEASES_ENDPOINT + "/1", HttpMethod.GET, new HttpEntity<Object>(headers), Release.class, headers
                );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }



    @Test
    public void postApplication() throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("applicationName", "app1");
        params.put("applicationDesc", "first app");
        params.put("applicationOwner", "me");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        HttpEntity<HashMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());
        this.restTemplate.exchange(BASE_APPLICATIONS_ENDPOINT, HttpMethod.POST, request, Application.class);

        ResponseEntity<List> response =
                this.restTemplate.exchange(
                        BASE_APPLICATIONS_ENDPOINT, HttpMethod.GET, new HttpEntity<Object>(headers), List.class, headers
                );

        int numberOfExpectedApps = NUMBER_OF_EXISTING_APPLICATIONS + 1;
        assertThat(response.getBody().size(), equalTo( Integer.valueOf(numberOfExpectedApps) ));
    }

    @Test
    public void postRelease() throws Exception {
        String url = String.format("%s%s%s", ROOT_URL, port, BASE_ENDPOINT + RELEASES_ENDPOINT);

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("description", "first release");
        params.put("releaseDate", "01/01/1900");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(params, new HttpHeaders());
        ResponseEntity<Release> responseEntity = restTemplate.exchange(BASE_RELEASES_ENDPOINT, HttpMethod.POST, request, Release.class);

        ResponseEntity<List> response = this.restTemplate.exchange(BASE_RELEASES_ENDPOINT, HttpMethod.GET, new HttpEntity<Object>(headers), List.class, headers);

        int numberOfExpectedReleases = NUMBER_OF_EXISTING_RELEASES + 1;
        assertThat(response.getBody().size(), equalTo( Integer.valueOf(numberOfExpectedReleases) ));
    }



    @Test
    public void postTicket() throws Exception {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        ResponseEntity<Application> appResponse =
                this.restTemplate.exchange(BASE_APPLICATIONS_ENDPOINT + "/1", HttpMethod.GET, new HttpEntity<Object>(headers), Application.class, headers);
        Application app = appResponse.getBody();

        ResponseEntity<Release> releaseResponse =
                this.restTemplate.exchange(BASE_RELEASES_ENDPOINT + "/1", HttpMethod.GET, new HttpEntity<Object>(headers), Release.class, headers);
        Release rel = releaseResponse.getBody();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("title", "My new feature");
        params.put("description", "best description");
        params.put("application", app);
        params.put("release", rel);
        params.put("status", "OPEN");

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(params, new HttpHeaders());
        this.restTemplate.exchange(BASE_TICKETS_ENDPOINT, HttpMethod.POST, request, Ticket.class);

        ResponseEntity<List> response = this.restTemplate.exchange(
                BASE_TICKETS_ENDPOINT, HttpMethod.GET, new HttpEntity<Object>(headers), List.class, headers
        );

        int numberOfExpectedTickets = NUMBER_OF_EXISTING_TICKETS + 1;
        assertThat(response.getBody().size(), equalTo( Integer.valueOf(numberOfExpectedTickets) ));
    }

    @Test
    public void updateApplication() throws Exception {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("id", String.valueOf(1));
        params.put("applicationName", "app1");
        params.put("applicationDesc", "first app");
        params.put("applicationOwner", "me");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        HttpEntity<HashMap<String, String>> request = new HttpEntity<>(params, new HttpHeaders());
        this.restTemplate.exchange(BASE_APPLICATIONS_ENDPOINT + "/1", HttpMethod.PUT, request, Application.class);

        ResponseEntity<Application> response =
            this.restTemplate.exchange(
                    BASE_APPLICATIONS_ENDPOINT + "/1", HttpMethod.GET, request, Application.class
            );

        assertThat(response.getBody().getName(), equalTo( "app1" ));
        assertThat(response.getBody().getDescription(), equalTo( "first app" ));
        assertThat(response.getBody().getOwner(), equalTo( "me" ));
    }

    @Test
    public void updateRelease() throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("releaseDesc", "first release");
        params.put("releaseDate", "01/01/1900");

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(params, new HttpHeaders());
        this.restTemplate.exchange(BASE_RELEASES_ENDPOINT + "/1", HttpMethod.PUT, request, Release.class);

        ResponseEntity<Release> response = this.restTemplate.exchange(BASE_RELEASES_ENDPOINT + "/1", HttpMethod.GET, request, Release.class);

        assertThat(response.getBody().getDescription(), equalTo( "first release" ));
        assertThat(response.getBody().getDate(), equalTo( "01/01/1900" ));
    }

    @Test
    public void updateTicket() throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("ticketTitle", "My Title");
        params.put("ticketDesc", "My Description");
        params.put("ticketStatus", "OPEN");

        HttpEntity<HashMap<String, Object>> request = new HttpEntity<HashMap<String, Object>>(params, new HttpHeaders());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("Authorization", BEARER + token);

        this.restTemplate.exchange(BASE_TICKETS_ENDPOINT + "/1", HttpMethod.PUT, request, Ticket.class);
        ResponseEntity<Ticket> response = this.restTemplate.exchange(BASE_TICKETS_ENDPOINT + "/1", HttpMethod.GET, request, Ticket.class);

        assertThat(response.getBody().getTitle(), equalTo( "My Title" ));
        assertThat(response.getBody().getDescription(), equalTo( "My Description" ));
        assertThat(response.getBody().getStatus(), equalTo( "OPEN" ));
    }

}
