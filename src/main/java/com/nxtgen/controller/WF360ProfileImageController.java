package com.WF360.controller;

import com.WF360.model.WF360ProfileImage;
import com.WF360.model.WF360ProfileImageUpdate;
import com.WF360.model.WF360User;
import com.WF360.services.WF360ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WF360ProfileImageController {

    @Autowired
    private WF360ProfileService profileService;

    @GetMapping("/getprofileimage")
    public ResponseEntity<WF360ProfileImage> getProfileImage(Authentication authentication) {
        return ResponseEntity.ok(profileService.profileImage(authentication.getName()));
    }

    @PutMapping("/saveprofileimage")
    public ResponseEntity<WF360User> saveProfileImage(
            Authentication authentication,
            @Valid @RequestBody WF360ProfileImageUpdate update) {
        return ResponseEntity.ok(profileService.updateProfileImage(authentication.getName(), update));
    }
}
