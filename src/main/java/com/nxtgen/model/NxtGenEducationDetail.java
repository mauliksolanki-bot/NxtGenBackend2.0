package com.nxtgen.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * One row of an employee's education history, stored as part of the JSON
 * array in {@code NXTGEN_APP_USER_MASTER.EDUCATION}. Field names mirror the
 * grid columns rendered in the UI profile form (FIELD_CODE = 'EDUCATION',
 * FIELD_TYPE = 'GRID' in {@code NXTGEN_FORM_FIELDS_MASTER}).
 */
@Data
public class NxtGenEducationDetail {

    @NotBlank
    private String degree;

    @NotBlank
    private String institution;

    private String university;

    @NotBlank
    private String yearOfPassing;

    @NotBlank
    private String percentage;
}
