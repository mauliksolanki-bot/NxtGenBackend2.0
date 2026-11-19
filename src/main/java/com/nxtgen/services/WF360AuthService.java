package com.WF360.services;

import com.WF360.model.WF360LoginCredentials;
import com.WF360.model.WF360LoginSession;
import com.WF360.repository.WF360UserRepository;
import com.WF360.security.WF360JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class WF360AuthService {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private WF360UserRepository userRepository;

  @Autowired private WF360JwtService jwtService;
  @Autowired private com.WF360.services.WF360UserAuditService audits;

  public WF360LoginSession login(WF360LoginCredentials credentials) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              credentials.getEmail(), credentials.getPassword()));
    } catch (org.springframework.security.core.AuthenticationException exception) {
      audits.log(
          null,
          "Authentication",
          "Failed login",
          "Failed login attempt for " + credentials.getEmail(),
          "FAILED",
          credentials.getEmail());
      throw exception;
    }

    var user = userRepository.findByEmailIgnoreCase(credentials.getEmail()).orElseThrow();
    String accessToken = jwtService.create(user.getEmpEmail(), user.getRoleName());
    audits.log(
        user.getEmpId(),
        "Authentication",
        "Login",
        "User logged in successfully.",
        "SUCCESS",
        user.getEmpEmail());

    return new WF360LoginSession(accessToken, "Bearer", jwtService.expiresIn(), user);
  }
}
