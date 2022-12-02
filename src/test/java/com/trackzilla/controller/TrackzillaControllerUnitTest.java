package com.trackzilla.controller;

import com.trackzilla.config.WebSecurityConfig;
import com.trackzilla.service.ApplicationService;
import com.trackzilla.service.ReleaseService;
import com.trackzilla.service.TicketService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TrackzillaController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TrackzillaControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ApplicationService applicationService;

    @MockBean
    ReleaseService releaseService;

    @MockBean
    TicketService ticketService;

    @Test
    public void getAllApplications() throws Exception {
        mockMvc.perform(get("/trackzilla/applications").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("[]"));

        verify(applicationService, times(1)).listApplications();
    }

    @Test
    public void getAllReleases() throws Exception {
        mockMvc.perform(get("/trackzilla/releases/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("[]"));

        verify(releaseService, times(1)).listReleases();
    }

    @Test
    public void getAllTickets() throws Exception {
        mockMvc.perform(get("/trackzilla/tickets/"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("[]"));

        verify(ticketService, times(1)).listTickets();
    }

}
