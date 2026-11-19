package com.WF360.model;

import lombok.Data;

@Data
public class WF360FormField {

    private Long id;
    private String fieldCode;
    private String fieldName;
    private String fieldType;
    private String isMandatory;
}
