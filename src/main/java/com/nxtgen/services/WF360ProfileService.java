package com.WF360.services;

import com.WF360.model.WF360BankDetail;
import com.WF360.model.WF360BankDetailUpdate;
import com.WF360.model.WF360EducationUpdate;
import com.WF360.model.WF360ProfileImage;
import com.WF360.model.WF360ProfileImageUpdate;
import com.WF360.model.WF360ProfileInformationUpdate;
import com.WF360.model.WF360User;
import com.WF360.repository.WF360BankDetailRepository;
import com.WF360.repository.WF360BankMasterRepository;
import com.WF360.repository.WF360UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WF360ProfileService {
  @Autowired private WF360UserRepository users;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private WF360BankDetailRepository bankDetails;

  @Autowired private WF360BankMasterRepository banks;

  public WF360User profile(String email) {
    return users
        .findByEmailIgnoreCase(email)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public WF360ProfileImage profileImage(String email) {
    return new WF360ProfileImage(profile(email).getProfileImage());
  }

  public WF360User updateEducation(String email, WF360EducationUpdate update) {
    var user = profile(email);
    try {
      users.updateEducation(
          user.getId().toString(), objectMapper.writeValueAsString(update.getEducation()), email);
      return profile(email);
    } catch (JsonProcessingException exception) {
      throw new IllegalArgumentException("Education details must be valid JSON.", exception);
    }
  }

  public WF360User updateProfileImage(String email, WF360ProfileImageUpdate update) {
    var user = profile(email);
    users.updateProfileImage(user.getId().toString(), update.getProfileImage(), email);
    return profile(email);
  }

  public WF360User updateProfileInformation(String email, WF360ProfileInformationUpdate update) {
    var user = profile(email);
    users.updatePhoneNumber(user.getId().toString(), "+91" + update.getPhoneNumber(), email);
    return profile(email);
  }

  public Optional<WF360BankDetail> bankDetails(String email) {
    return bankDetails.findByEmployeeId(profile(email).getEmpId());
  }

  public java.util.List<com.WF360.model.WF360BankMaster> bankMaster() {
    return banks.findAllActive();
  }

  public WF360BankDetail saveBankDetails(String email, WF360BankDetailUpdate update) {
    if (!isBankDetailsUpdateAllowed(LocalDate.now())) {
      throw new WF360BankDetailUpdateWindowException();
    }

    var user = profile(email);
    update.setIfscCode(update.getIfscCode().trim().toUpperCase());
    var bank =
        banks
            .findActiveByBankName(update.getBankName().trim())
            .orElseThrow(() -> new IllegalArgumentException("Select a valid bank."));
    if (update.getAccountNumber().length() != bank.getAccountNumberLength()) {
      throw new IllegalArgumentException(
          "Account number for "
              + bank.getBankName()
              + " must contain "
              + bank.getAccountNumberLength()
              + " digits.");
    }
    bankDetails.save(user.getEmpId(), update, email);
    return bankDetails.findByEmployeeId(user.getEmpId()).orElseThrow();
  }

  private boolean isBankDetailsUpdateAllowed(LocalDate date) {
    return date.getDayOfMonth() >= 5 && date.getDayOfMonth() < 25;
  }
}
