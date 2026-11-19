package com.WF360.controller;
import com.WF360.model.WF360ProfileInformationUpdate;
import com.WF360.model.WF360BankDetail;
import com.WF360.model.WF360BankDetailUpdate;
import com.WF360.model.WF360BankMaster;
import java.util.List;
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
public class WF360ProfileController {

    @Autowired
    private WF360ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<WF360User> profile(Authentication authentication) {
        return ResponseEntity.ok(profileService.profile(authentication.getName()));
    }

    @PutMapping("/saveprofileinformation")
    public ResponseEntity<WF360User> saveProfileInformation(
            Authentication authentication,
            @Valid @RequestBody WF360ProfileInformationUpdate update) {
        return ResponseEntity.ok(profileService.updateProfileInformation(authentication.getName(), update));
    }

    @GetMapping("/bankdetails")
    public ResponseEntity<WF360BankDetail> bankDetails(Authentication authentication) {
        return profileService.bankDetails(authentication.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/banks")
    public ResponseEntity<List<WF360BankMaster>> banks() {
        return ResponseEntity.ok(profileService.bankMaster());
    }

    @PutMapping("/savebankdetails")
    public ResponseEntity<WF360BankDetail> saveBankDetails(
            Authentication authentication,
            @Valid @RequestBody WF360BankDetailUpdate update) {
        return ResponseEntity.ok(profileService.saveBankDetails(authentication.getName(), update));
    }
}
