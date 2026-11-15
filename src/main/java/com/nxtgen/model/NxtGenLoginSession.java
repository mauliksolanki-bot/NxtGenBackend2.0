package com.nxtgen.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NxtGenLoginSession {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private NxtGenUser user;
}
