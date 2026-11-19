package com.WF360.model;

import lombok.Data;

@Data
public class WF360BankDetail {

  private Long id;
  private String empId;
  private String accountNumber;
  private String ifscCode;
  private String bankName;
  private String branchName;
  private String accountType;
}
