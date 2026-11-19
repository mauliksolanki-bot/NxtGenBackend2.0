package com.WF360.model;

import lombok.Data;

@Data
public class WF360RoleDesignation {
    private Long id;
    private String roleName;
    private String roleDesc;
    private String roleLevel;
    private String roleGroup;
    private String isActive;
}
