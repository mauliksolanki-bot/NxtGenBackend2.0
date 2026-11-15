package com.nxtgen.controller;

import com.nxtgen.model.NxtGenEducationUpdate;
import com.nxtgen.model.NxtGenUser;
import com.nxtgen.services.NxtGenProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NxtGenEducationController {

    private final NxtGenProfileService profileService;

    public NxtGenEducationController(NxtGenProfileService profileService) {
        this.profileService = profileService;
    }

    @PutMapping("/saveeducationdetails")
    public ResponseEntity<NxtGenUser> saveEducationDetails(
            Authentication authentication,
            @Valid @RequestBody NxtGenEducationUpdate educationUpdate) {
        return ResponseEntity.ok(profileService.updateEducation(authentication.getName(), educationUpdate));
    }
}
