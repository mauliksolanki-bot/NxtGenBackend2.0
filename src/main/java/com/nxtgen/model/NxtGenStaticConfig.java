package com.nxtgen.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * A single row of {@code NXTGEN_STATIC_CONFIG} — a generic, backend-driven UI/behavior
 * configuration keyed by {@code confCode} (for example, the field layout and validation rules
 * for the "Edit education details" popup). {@code staticJson} is kept as a raw {@link JsonNode}
 * since its shape is entirely defined by whatever UI feature consumes it.
 */
@Data
public class NxtGenStaticConfig {

    private Long id;
    private String confCode;
    private String confCategory;
    private JsonNode staticJson;
    private String isActive;
}
