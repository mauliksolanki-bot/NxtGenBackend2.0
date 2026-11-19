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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private WF360UserRepository userRepository;

    @Autowired
    private WF360JwtService jwtService;

    public WF360LoginSession login(WF360LoginCredentials credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));

        var user = userRepository.findByEmailIgnoreCase(credentials.getEmail()).orElseThrow();
        String accessToken = jwtService.create(user.getEmpEmail(), user.getRoleName());

        return new WF360LoginSession(
                accessToken,
                "Bearer",
                jwtService.expiresIn(),
                user);
    }
}
