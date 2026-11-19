package com.WF360.controller;

import com.WF360.constants.WF360ApiPaths;
import com.WF360.model.WF360LoginCredentials;
import com.WF360.model.WF360LoginSession;
import com.WF360.services.WF360AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WF360ApiPaths.AUTH)
public class WF360AuthController {

    @Autowired
    private WF360AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<WF360LoginSession> login(@Valid @RequestBody WF360LoginCredentials credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }
}
