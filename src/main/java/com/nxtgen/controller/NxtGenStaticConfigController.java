package com.nxtgen.controller;

import com.nxtgen.model.NxtGenStaticConfig;
import com.nxtgen.services.NxtGenStaticConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Serves backend-driven UI configuration rows (popup layouts, field definitions, etc.). */
@RestController
@RequestMapping("/config")
public class NxtGenStaticConfigController {

    private final NxtGenStaticConfigService staticConfigService;

    public NxtGenStaticConfigController(NxtGenStaticConfigService staticConfigService) {
        this.staticConfigService = staticConfigService;
    }

    @GetMapping("/editedudetailspopup")
    public ResponseEntity<NxtGenStaticConfig> getEduDetailsPopup() {
        String confCode = "EDIT_EDU_DETAILS";
        return staticConfigService.findActiveByCode(confCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
