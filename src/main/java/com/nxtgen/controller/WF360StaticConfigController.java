package com.WF360.controller;

import com.WF360.model.WF360StaticConfig;
import com.WF360.services.WF360StaticConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Serves backend-driven UI configuration rows (popup layouts, field definitions, etc.). */
@RestController
@RequestMapping("/config")
public class WF360StaticConfigController {

    @Autowired
    private WF360StaticConfigService staticConfigService;

    @GetMapping("/editedudetailspopup")
    public ResponseEntity<WF360StaticConfig> getEduDetailsPopup() {
        String confCode = "EDIT_EDU_DETAILS";
        return staticConfigService.findActiveByCode(confCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/editroledesignationpopup")
    public ResponseEntity<WF360StaticConfig> getRoleDesignationPopup() {
        return staticConfigService.findActiveByCode("EDIT_ROLE_DESIGNATION")
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/deleteconfirm")
    public ResponseEntity<WF360StaticConfig> getDeleteConfirmationPopup() {
        return staticConfigService.findActiveByCode("DELETE_CONFIRM")
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
