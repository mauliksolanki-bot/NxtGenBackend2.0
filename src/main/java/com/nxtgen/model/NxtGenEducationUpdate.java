package com.nxtgen.model;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NxtGenEducationUpdate {

    @NotNull
    private JsonNode education;
}
