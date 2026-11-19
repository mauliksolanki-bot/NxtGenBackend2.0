package com.WF360.controller;

import com.WF360.model.WF360Navigation;
import com.WF360.services.WF360NavigationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/navigation")
public class WF360NavigationController {

    @Autowired
    private WF360NavigationService navigationService;

    @GetMapping
    public ResponseEntity<List<WF360Navigation>> getNavigation(Authentication authentication) {
        return ResponseEntity.ok(navigationService.findNavigationForUser(authentication.getName()));
    }
}
