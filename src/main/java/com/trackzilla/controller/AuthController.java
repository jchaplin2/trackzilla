package com.trackzilla.controller;

import com.trackzilla.entity.*;
import com.trackzilla.repository.RoleRepository;
import com.trackzilla.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  public static final String ERROR_USERNAME_TAKEN = "ERROR! Username is already taken!";
  public static final String ERROR_EMAIL_INUSE = "ERROR! Email is already in use!";
  public static final String ERROR_ROLE_NOT_FOUND =  "ERROR! Role is not found.";
  public static final String SUCCESS_MESSAGE = "User registered successfully!";

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
    User user = new User(signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
               signUpRequest.getUsername());

    Set<Role> roles = new HashSet<>();
    Optional<Set<String>> role =  Optional.of(signUpRequest.getRole());

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
}
