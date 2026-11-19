package com.WF360.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class WF360Holiday {
  private String holidayName;
  private LocalDate onDate;
}
