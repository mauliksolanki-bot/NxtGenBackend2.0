package com.WF360.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class WF360EducationUpdate {

    @NotNull
    private List<@Valid WF360EducationDetail> education;
}
