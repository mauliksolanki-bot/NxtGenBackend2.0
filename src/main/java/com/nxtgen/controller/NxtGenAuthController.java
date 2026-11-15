package com.nxtgen.controller;

import com.nxtgen.constants.NxtGenApiPaths;
import com.nxtgen.model.NxtGenLoginCredentials;
import com.nxtgen.model.NxtGenLoginSession;
import com.nxtgen.services.NxtGenAuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(NxtGenApiPaths.AUTH)
public class NxtGenAuthController {

    private final NxtGenAuthService authService;

    public NxtGenAuthController(NxtGenAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<NxtGenLoginSession> login(@Valid @RequestBody NxtGenLoginCredentials credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }
}
