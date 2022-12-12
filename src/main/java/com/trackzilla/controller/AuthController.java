package com.trackzilla.controller;

import com.trackzilla.entity.*;
import com.trackzilla.repository.RoleRepository;
import com.trackzilla.repository.UserRepository;
import com.trackzilla.security.JwtUtils;
import com.trackzilla.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  public static final String ERROR_USERNAME_TAKEN = "ERROR! Username is already taken!";
  public static final String ERROR_EMAIL_INUSE = "ERROR! Email is already in use!";
  public static final String ERROR_ROLE_NOT_FOUND =  "ERROR! Role is not found.";
  public static final String SUCCESS_MESSAGE = "User registered successfully!";

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse(ERROR_USERNAME_TAKEN));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse(ERROR_EMAIL_INUSE));
    }

    // Create new user's account
    User user = new User();
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(encoder.encode(signUpRequest.getPassword()));
    user.setUsername(signUpRequest.getUsername());

    Set<Role> roles = new HashSet<>();
    Optional<Set<String>> role =  Optional.ofNullable(signUpRequest.getRole());

    role.ifPresentOrElse(
            (roleSet) -> {
              roleSet.forEach(roleVal -> {
                ERole erole = ERole.valueOf(roleVal);
                switch (erole) {
                  case ROLE_APPLICATION_USER:
                    Role appRole = roleRepository.findByName(ERole.ROLE_APPLICATION_USER)
                            .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND));
                    roles.add(appRole);

                    break;
                  case ROLE_RELEASE_USER:
                    Role releaseRole = roleRepository.findByName(ERole.ROLE_RELEASE_USER)
                            .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND));
                    roles.add(releaseRole);

                    break;
                  case ROLE_TICKET_USER:
                    Role ticketRole = roleRepository.findByName(ERole.ROLE_TICKET_USER)
                            .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND));
                    roles.add(ticketRole);

                    break;
                  default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND));
                    roles.add(userRole);
                }
              });
            },
            () -> {
              Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                      .orElseThrow(() -> new RuntimeException(ERROR_ROLE_NOT_FOUND));
              roles.add(userRole);
            }
    );

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse(SUCCESS_MESSAGE));
  }

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles));
  }
}
