package com.wf360.model;

public enum MessageStatus {
    FM_001_SAVED_SUCCESSFULLY("FM-001", "Saved"),
    FM_003_VALIDATION_EXCEPTION("FM-003", "Validation failed"),
    FM_004_SERVICE_EXCEPTION("FM-004", "Request failed"),
    FM_005_INVALID_SESSION("FM-005", "Authentication failed");

    private final String code;
    private final String title;

    MessageStatus(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}
