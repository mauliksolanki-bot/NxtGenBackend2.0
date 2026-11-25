package com.wf360.model;

import java.util.Map;
import lombok.Data;

@Data
public class MessageInfo {
    private final String id;
    private final String status;
    private final String code;
    private final String title;
    private final String details;
    private final String type;
    private Map<String, Object> extra;

    public MessageInfo(String id, String status, String code, String title, String details, String type) {
        this.id = id;
        this.status = status;
        this.code = code;
        this.title = title;
        this.details = details;
        this.type = type;
    }
}
