package com.WF360.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * One row of an employee's education history, stored as part of the JSON array in {@code
 * WF360_APP_USER_MASTER.EDUCATION}. Field names mirror the grid columns rendered in the UI profile
 * form (FIELD_CODE = 'EDUCATION', FIELD_TYPE = 'GRID' in {@code WF360_FORM_FIELDS_MASTER}).
 */
@Data
public class WF360EducationDetail {

  @NotBlank private String degree;

  @NotBlank private String institution;

  private String university;

  @NotBlank private String yearOfPassing;

  @NotBlank private String percentage;
}
