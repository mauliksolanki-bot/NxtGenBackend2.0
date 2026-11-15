package com.nxtgen.controller;

import com.nxtgen.model.NxtGenProfileImage;
import com.nxtgen.model.NxtGenProfileImageUpdate;
import com.nxtgen.model.NxtGenUser;
import com.nxtgen.services.NxtGenProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NxtGenProfileImageController {

    private final NxtGenProfileService profileService;

    public NxtGenProfileImageController(NxtGenProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/getprofileimage")
    public ResponseEntity<NxtGenProfileImage> getProfileImage(Authentication authentication) {
        return ResponseEntity.ok(profileService.profileImage(authentication.getName()));
    }

    @PutMapping("/saveprofileimage")
    public ResponseEntity<NxtGenUser> saveProfileImage(
            Authentication authentication,
            @Valid @RequestBody NxtGenProfileImageUpdate update) {
        return ResponseEntity.ok(profileService.updateProfileImage(authentication.getName(), update));
    }
}
