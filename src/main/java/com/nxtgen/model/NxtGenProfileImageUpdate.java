package com.nxtgen.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NxtGenProfileImageUpdate {

    @NotBlank private String profileImage;
}
