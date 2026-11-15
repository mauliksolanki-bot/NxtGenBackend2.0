package com.nxtgen.services;

import com.nxtgen.model.NxtGenLoginCredentials;
import com.nxtgen.model.NxtGenLoginSession;
import com.nxtgen.repository.NxtGenUserRepository;
import com.nxtgen.security.NxtGenJwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class NxtGenAuthService {

    private final AuthenticationManager authenticationManager;
    private final NxtGenUserRepository userRepository;
    private final NxtGenJwtService jwtService;

    public NxtGenAuthService(
            AuthenticationManager authenticationManager,
            NxtGenUserRepository userRepository,
            NxtGenJwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public NxtGenLoginSession login(NxtGenLoginCredentials credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));

        var user = userRepository.findByEmailIgnoreCase(credentials.getEmail()).orElseThrow();
        String accessToken = jwtService.create(user.getEmpEmail(), user.getRoleName());

        return new NxtGenLoginSession(
                accessToken,
                "Bearer",
                jwtService.expiresIn(),
                user);
    }
}
