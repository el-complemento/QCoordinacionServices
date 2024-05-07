package com.ingsoftware.qcoordinacion_fhir_service.controllers;

import com.ingsoftware.qcoordinacion_fhir_service.services.PractitionerService;
import org.hl7.fhir.r5.model.Practitioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/practicioners")
public class PractitionerController {
    @Autowired
    private PractitionerService practitionerService;

    @PostMapping
    public ResponseEntity<String> createPracticioner(@RequestBody String practitioner) {
        String practitionerId = practitionerService.createPractitioner(practitioner);
        return ResponseEntity.ok("Patient created with ID: " + practitionerId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Practitioner> getPractitioner(@RequestBody String id){
        Practitioner practitioner = practitionerService.getPractitioner(id);
        return ResponseEntity.ok(practitioner);
    }
}
