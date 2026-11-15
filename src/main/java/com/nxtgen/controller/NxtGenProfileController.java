package com.nxtgen.controller;
import com.nxtgen.model.NxtGenUser;
import com.nxtgen.services.NxtGenProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class NxtGenProfileController {

    private final NxtGenProfileService profileService;

    public NxtGenProfileController(NxtGenProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<NxtGenUser> profile(Authentication authentication) {
        return ResponseEntity.ok(profileService.profile(authentication.getName()));
    }
}
