package com.nxtgen.model;

import lombok.Data;

@Data
public class NxtGenFormField {

    private Long id;
    private String fieldCode;
    private String fieldName;
    private String fieldType;
    private String isMandatory;
}
