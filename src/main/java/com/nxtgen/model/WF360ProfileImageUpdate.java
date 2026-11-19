package com.WF360.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class WF360ProfileImageUpdate {

    @NotBlank private String profileImage;
}
