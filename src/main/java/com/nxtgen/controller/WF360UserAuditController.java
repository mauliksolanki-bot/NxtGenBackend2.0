package com.WF360.controller;

import com.WF360.model.WF360UserAuditPage;
import com.WF360.services.WF360UserAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audit/user-access-activities")
public class WF360UserAuditController {
  @Autowired private WF360UserAuditService service;

  @GetMapping
  public ResponseEntity<WF360UserAuditPage> all(
      Authentication authentication,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int pageSize,
      @RequestParam(required = false) String search) {
    return ResponseEntity.ok(service.all(authentication.getName(), search, page, pageSize));
  }
}
