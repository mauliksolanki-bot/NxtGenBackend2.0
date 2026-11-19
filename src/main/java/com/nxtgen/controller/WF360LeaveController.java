package com.WF360.controller;

import com.WF360.model.WF360Holiday;
import com.WF360.model.WF360LeaveBalance;
import com.WF360.model.WF360LeaveCategory;
import com.WF360.model.WF360LeaveDocument;
import com.WF360.model.WF360LeaveHistory;
import com.WF360.services.WF360LeaveService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/leave-management")
public class WF360LeaveController {
  @Autowired private WF360LeaveService service;

  @GetMapping("/leave-categories")
  public ResponseEntity<List<WF360LeaveCategory>> categories() {
    return ResponseEntity.ok(service.categories());
  }

  @GetMapping("/holiday-calendar")
  public ResponseEntity<List<WF360Holiday>> holidays() {
    return ResponseEntity.ok(service.holidays());
  }

  @GetMapping("/employee-leave-balances")
  public ResponseEntity<List<WF360LeaveBalance>> balance(Authentication authentication) {
    return ResponseEntity.ok(service.balances(authentication.getName()));
  }

  @GetMapping("/employee-leave-history")
  public ResponseEntity<List<WF360LeaveHistory>> history(Authentication authentication) {
    return ResponseEntity.ok(service.history(authentication.getName()));
  }

  @GetMapping("/manager-leave-approvals")
  public ResponseEntity<List<WF360LeaveHistory>> approvals(Authentication authentication) {
    return ResponseEntity.ok(service.approvals(authentication.getName()));
  }

  @GetMapping("/leave-applications/{id}/supporting-document")
  public ResponseEntity<byte[]> document(Authentication authentication, @PathVariable long id) {
    WF360LeaveDocument document = service.document(authentication.getName(), id);
    MediaType mediaType = MediaType.parseMediaType(document.mimeType());
    return ResponseEntity.ok()
        .contentType(mediaType)
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            ContentDisposition.inline().filename(document.name()).build().toString())
        .body(document.content());
  }

  @PostMapping(value = "/leave-applications", consumes = "multipart/form-data")
  public ResponseEntity<Void> apply(
      Authentication authentication,
      @RequestParam String leaveType,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
      @RequestParam String reason,
      @RequestParam(required = false) MultipartFile document) {
    service.apply(authentication.getName(), leaveType, fromDate, toDate, reason, document);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/leave-applications/{id}/cancellation")
  public ResponseEntity<Void> cancel(Authentication authentication, @PathVariable long id) {
    service.cancel(authentication.getName(), id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/leave-applications/{id}/approval-decision")
  public ResponseEntity<Void> decide(
      Authentication authentication, @PathVariable long id, @RequestParam String status) {
    if (!List.of("APPROVED", "REJECTED").contains(status))
      throw new IllegalArgumentException("Invalid leave decision.");
    service.decide(authentication.getName(), id, status);
    return ResponseEntity.noContent().build();
  }
}
