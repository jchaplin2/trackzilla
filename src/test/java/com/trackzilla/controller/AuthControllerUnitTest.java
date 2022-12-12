package com.trackzilla.controller;

import com.trackzilla.config.WebSecurityConfig;
import com.trackzilla.entity.ERole;
import com.trackzilla.entity.Role;
import com.trackzilla.repository.RoleRepository;
import com.trackzilla.repository.UserRepository;
import com.trackzilla.security.JwtUtils;
import com.trackzilla.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import({AuthController.class})
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = JwtUtils.class)
public class AuthControllerUnitTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void postSignup() throws Exception {
        Mockito.when(roleRepository.findByName(ERole.ROLE_APPLICATION_USER))
                .thenReturn(Optional.of(new Role(ERole.ROLE_APPLICATION_USER)));

        Mockito.when(roleRepository.findByName(ERole.ROLE_TICKET_USER))
                .thenReturn(Optional.of(new Role(ERole.ROLE_TICKET_USER)));

        Mockito.when(roleRepository.findByName(ERole.ROLE_RELEASE_USER))
                .thenReturn(Optional.of(new Role(ERole.ROLE_RELEASE_USER)));

        Mockito.when(roleRepository.findByName(ERole.ROLE_USER))
                .thenReturn(Optional.of(new Role(ERole.ROLE_USER)));

        String successJSONResponse = "{ \"message\" : \"" +AuthController.SUCCESS_MESSAGE+ "\" }";

        mockMvc.perform(post("/api/auth/signup").with(csrf()).with(httpBasic("admin", "123456"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"username\":\"admin\", \"email\": \"admin@abc.com\", \"password\":\"123456\", \"role\": [\"ROLE_APPLICATION_USER\", \"ROLE_TICKET_USER\", \"ROLE_RELEASE_USER\", \"ROLE_USER\"] }"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(successJSONResponse));

    }

    @Test
    public void postSignupNoRoles() throws Exception {

        Mockito.when(roleRepository.findByName(ERole.ROLE_USER))
                .thenReturn(Optional.of(new Role(ERole.ROLE_USER)));

        String successJSONResponse = "{ \"message\" : \"" +AuthController.SUCCESS_MESSAGE+ "\" }";

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"username\":\"admin\", \"email\": \"admin@abc.com\", \"password\":\"123456\" }"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(successJSONResponse));
    }

    @Test
    public void postSignupUsernameError() throws Exception {

        Mockito.when(userRepository.existsByUsername("admin"))
                .thenReturn(Boolean.TRUE);

        String successJSONResponse = "{ \"message\" : \"" +AuthController.ERROR_USERNAME_TAKEN+ "\" }";

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"username\":\"admin\", \"email\": \"admin@abc.com\", \"password\":\"123456\", \"role\": [\"ROLE_APPLICATION_USER\", \"ROLE_TICKET_USER\", \"ROLE_RELEASE_USER\", \"ROLE_USER\"] }"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(successJSONResponse));
    }

    @Test
    public void postSignupEmailError() throws Exception {

        Mockito.when(userRepository.existsByUsername("admin"))
                .thenReturn(Boolean.FALSE);

        Mockito.when(userRepository.existsByEmail("admin@abc.com"))
                .thenReturn(Boolean.TRUE);

        String successJSONResponse = "{ \"message\" : \"" +AuthController.ERROR_EMAIL_INUSE+ "\" }";

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"username\":\"admin\", \"email\": \"admin@abc.com\", \"password\":\"123456\", \"role\": [\"ROLE_APPLICATION_USER\", \"ROLE_TICKET_USER\", \"ROLE_RELEASE_USER\", \"ROLE_USER\"] }"))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(successJSONResponse));
    }

}
