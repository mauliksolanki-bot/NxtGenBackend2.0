package com.WF360.controller;

import com.WF360.model.WF360EducationUpdate;
import com.WF360.model.WF360User;
import com.WF360.services.WF360ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WF360EducationController {

  @Autowired private WF360ProfileService profileService;

  @PutMapping("/employees/me/education-details")
  public ResponseEntity<WF360User> saveEducationDetails(
      Authentication authentication, @Valid @RequestBody WF360EducationUpdate educationUpdate) {
    return ResponseEntity.ok(
        profileService.updateEducation(authentication.getName(), educationUpdate));
  }
}
