package com.WF360.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class WF360RoleDesignationUpdate {
  @NotBlank
  @Size(max = 120)
  private String roleName;

  @Size(max = 500)
  private String roleDesc;

  @NotBlank
  @Pattern(regexp = "L(?:[1-9]|1[01])")
  private String roleLevel;

  @Pattern(regexp = "[YN]")
  private String isActive;
}
