package com.nxtgen.controller;

import com.nxtgen.model.NxtGenNavigation;
import com.nxtgen.services.NxtGenNavigationService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/navigation")
public class NxtGenNavigationController {

    private final NxtGenNavigationService navigationService;

    public NxtGenNavigationController(NxtGenNavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @GetMapping
    public ResponseEntity<List<NxtGenNavigation>> getNavigation(Authentication authentication) {
        return ResponseEntity.ok(navigationService.findNavigationForUser(authentication.getName()));
    }
}
