package com.nxtgen.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class NxtGenEducationUpdate {

    @NotNull
    private List<@Valid NxtGenEducationDetail> education;
}
