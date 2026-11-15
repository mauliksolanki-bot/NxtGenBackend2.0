package com.nxtgen.controller;

import com.nxtgen.model.NxtGenFormField;
import com.nxtgen.repository.NxtGenFormRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retriveform")
public class NxtGenFormController {

    private final NxtGenFormRepository formRepository;

    public NxtGenFormController(NxtGenFormRepository formRepository) {
        this.formRepository = formRepository;
    }

    @GetMapping("/{formCode}")
    public ResponseEntity<List<NxtGenFormField>> getFields(@PathVariable("formCode") String formCode) {
        return ResponseEntity.ok(formRepository.findActiveFields(formCode));
    }
}
