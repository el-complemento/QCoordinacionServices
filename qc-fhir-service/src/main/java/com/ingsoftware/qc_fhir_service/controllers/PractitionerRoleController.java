package com.ingsoftware.qc_fhir_service.controllers;

import ca.uhn.fhir.rest.annotation.Search;
import com.ingsoftware.qc_fhir_service.services.PractitionerRoleService;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/practitioner-roles")
public class PractitionerRoleController {
    @Autowired
    private PractitionerRoleService practitionerRoleService;

    @PostMapping
    public ResponseEntity<String> createPractitionerRole(@RequestBody String practitioner) {
        String practitionerRoleId = practitionerRoleService.createPractitionerRole(practitioner);
        return ResponseEntity.ok(": " + practitionerRoleId);
    }
    @GetMapping("/rol/{id}")
    @Search
    public ResponseEntity<String> getPractitionerRole(@PathVariable String id) {
        PractitionerRole rolId = practitionerRoleService.getPractitionerRole(id);
        String rolEnString = rolId.getCodeFirstRep().getCoding().get(0).getCode();
        return ResponseEntity.ok(rolEnString);
    }
    @GetMapping("/practitioners/{role}")
    public ResponseEntity<Bundle> getPractitionersWithRole(@PathVariable String role) {
        Bundle practitioner = practitionerRoleService.getPractitionersByRole(role);
        return ResponseEntity.ok(practitioner);
    }
    @GetMapping("/disponibilidad/{id}")
    public ResponseEntity<String> getPractitionerDisponiblidad(@PathVariable String id) {
        String disponibilidad = practitionerRoleService.getPractitionerDisponibilidad(id);
        return ResponseEntity.ok(disponibilidad);
    }
    @GetMapping("/id/{role}")
    public ResponseEntity<List<String>> getPractitionersIDWithRole(@PathVariable String role) {
        Bundle practitioners = practitionerRoleService.getPractitionersByRole(role);
        List<String> cedulas = new ArrayList<>();
        if (practitioners != null && practitioners.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : practitioners.getEntry()) {
                Resource resource = entry.getResource();
                if (resource instanceof Practitioner practitioner) {
                    cedulas.add(practitioner.getIdPart());
                }
           }
        }
        return ResponseEntity.ok(cedulas);
    }
}