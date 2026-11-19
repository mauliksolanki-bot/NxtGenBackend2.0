package com.WF360.model;

import lombok.Data;

@Data
public class WF360DataGridColumn {
    private String columnName;
    private Integer columnOrder;
    private String uiDisplayName;
    private String width;
    private String sortable;
    private String isHyperlink;
    private String type;
    private String showFilter;
}
