package com.WF360.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class WF360LeaveHistory {
  private long id;
  private String leaveType;
  private LocalDate fromDate;
  private LocalDate toDate;
  private String reason;
  private String status;
  private String approvedBy;
  private LocalDate appliedOn;
  private boolean documentAttached;
}
