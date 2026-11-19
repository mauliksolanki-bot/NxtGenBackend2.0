package com.WF360.controller;

import com.WF360.model.WF360FormField;
import com.WF360.repository.WF360FormRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forms")
public class WF360FormController {

  @Autowired private WF360FormRepository formRepository;

  @GetMapping("/details")
  public ResponseEntity<List<WF360FormField>> getFormFieldDetails(
      @RequestParam("formCode") String formCode) {
    return ResponseEntity.ok(formRepository.findActiveFields(formCode));
  }
}
