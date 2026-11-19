package com.WF360.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class WF360User {

    private UUID id;

    private String empId;

    private String empFirstName;

    private String empLastName;

    private String empEmail;

    @JsonIgnore
    private String hashPassword;

    private Long primaryRoleId;

    private String roleName;

    private String roleLevel;

    private String isActive;

    private String createdBy;

    private OffsetDateTime createdDate;

    private String updatedBy;

    private OffsetDateTime updatedDate;

    private List<WF360EducationDetail> education;

    private String profileImage;

    private String phoneNumber;

    private LocalDate dateOfJoining;

    private String workLocation;

    private String reportingManager;

    private String designation;

    private String panNumber;

    private String aadharNumber;
}
