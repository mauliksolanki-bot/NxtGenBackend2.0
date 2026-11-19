package com.WF360.controller;

import com.WF360.model.WF360RoleDesignationGrid;
import com.WF360.model.WF360RoleDesignationUpdate;
import com.WF360.services.WF360RoleDesignationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WF360RoleDesignationController {
  @Autowired private WF360RoleDesignationService service;

  @GetMapping("/role-designations/grid")
  public ResponseEntity<WF360RoleDesignationGrid> grid(
      Authentication authentication,
      @RequestParam String roleGroup,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int pageSize) {
    return ResponseEntity.ok(service.grid(authentication.getName(), roleGroup, page, pageSize));
  }

  @GetMapping("/role-designations/groups")
  public ResponseEntity<java.util.List<String>> groups(Authentication authentication) {
    return ResponseEntity.ok(service.groups(authentication.getName()));
  }

  @PutMapping("/role-designations/{id}")
  public ResponseEntity<Void> update(
      Authentication authentication,
      @PathVariable long id,
      @Valid @RequestBody WF360RoleDesignationUpdate update) {
    service.update(authentication.getName(), id, update);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/role-designations/{id}")
  public ResponseEntity<Void> delete(Authentication authentication, @PathVariable long id) {
    service.delete(authentication.getName(), id);
    return ResponseEntity.noContent().build();
  }
}
