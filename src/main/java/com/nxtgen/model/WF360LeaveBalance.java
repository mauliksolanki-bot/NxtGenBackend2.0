package com.WF360.model;

import lombok.Data;

@Data
public class WF360LeaveBalance {
  private String leaveType;
  private int totalLeave;
  private int availableLeave;
}
