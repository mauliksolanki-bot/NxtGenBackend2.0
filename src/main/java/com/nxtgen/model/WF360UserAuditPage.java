package com.WF360.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WF360UserAuditPage {
  private List<WF360UserAudit> rows;
  private long totalRecords;
  private int page;
  private int pageSize;
}
