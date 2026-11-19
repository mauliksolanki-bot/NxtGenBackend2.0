package com.WF360.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class WF360ProfileInformationUpdate {

  @NotBlank
  @Pattern(regexp = "^\\d{10}$", message = "Phone number must contain exactly 10 digits.")
  private String phoneNumber;
}
