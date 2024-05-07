package com.ingsoftware.qcoordinacion_fhir_service.controllers;

import com.ingsoftware.qcoordinacion_fhir_service.services.PractitionerRoleService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/practitioner-roles")
public class PractitionerRoleController {

    /*
    @Autowired
    private PractitionerRoleService practitionerRoleService;

    @PostMapping
    public ResponseEntity<String> createPractitionerRole(@RequestBody String practitioner) {
        String practitionerRoleId = practitionerRoleService.createPractitionerRole(practitioner);
        return ResponseEntity.ok(": " + practitionerRoleId);
    }

    @GetMapping("/{role}")
    public ResponseEntity<Bundle> getPractitioner(@RequestBody String role){
        Bundle practitioner = practitionerRoleService.getPractitionersByRole(role);
        return ResponseEntity.ok(practitioner);
    }

    // The following method is causing compilation error, so it's commented out

    @GetMapping("/nombresRoles/{role}")
    public ResponseEntity<List<String>> getPractitionersNameWithRole(@RequestBody String role) {
        Bundle practitioners = practitionerRoleService.getPractitionersByRole(role);
        List<String> nombres = new ArrayList<>();
        if (practitioners != null && practitioners.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : practitioners.getEntry()) {
                Resource resource = entry.getResource();
                if (resource instanceof Practitioner) {
                    Practitioner practitioner = (Practitioner) resource;
                    String name = practitioner.getNameFirstRep().getFamily();
                    nombres.add(name);
                }
            }
        }
        return ResponseEntity.ok(nombres);
    }
    */
}
