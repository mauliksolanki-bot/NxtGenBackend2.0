package com.WF360.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WF360LoginSession {

  private String accessToken;
  private String tokenType;
  private long expiresIn;
  private WF360User user;
}
