package com.WF360.model;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class WF360UserAudit {
  private String id;
  private String empId;
  private String employeeName;
  private String activityCategory;
  private String activityAction;
  private String activityDetails;
  private String activityStatus;
  private OffsetDateTime activityAt;
}
