package com.WF360.controller;
import com.WF360.model.WF360BankDetailsGrid;
import com.WF360.services.WF360PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class WF360PayrollController { @Autowired private WF360PayrollService service; @GetMapping("/bankdetailsgrid") public ResponseEntity<WF360BankDetailsGrid> bankDetailsGrid(Authentication authentication) { return ResponseEntity.ok(service.bankGrid(authentication.getName())); } }
