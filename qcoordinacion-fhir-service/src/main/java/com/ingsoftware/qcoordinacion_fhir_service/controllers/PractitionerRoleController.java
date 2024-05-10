package com.ingsoftware.qcoordinacion_fhir_service.controllers;

import com.ingsoftware.qcoordinacion_fhir_service.services.PractitionerRoleService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.PractitionerRole;
import org.hl7.fhir.r5.model.Resource;
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
    @GetMapping("/{id}")
    public ResponseEntity<String> getPractitionerRole(@PathVariable String id) {
        PractitionerRole rolId = practitionerRoleService.getPractitionerRole(id);
        String rolEnString = rolId.getCodeFirstRep().getCoding().get(0).getCode();
        return ResponseEntity.ok(rolEnString);
    }
    @GetMapping("{role}")
    public ResponseEntity<Bundle> getPractitionersWithRole(@PathVariable String role) {
        Bundle practitioner = practitionerRoleService.getPractitionersByRole(role);
        return ResponseEntity.ok(practitioner);
    }
    @GetMapping("/nombresRoles/{role}")
    public ResponseEntity<List<String>> getPractitionersNameWithRole(@PathVariable String role) {
        Bundle practitioners = practitionerRoleService.getPractitionersByRole(role);
        List<String> nombres = new ArrayList<>();
        if (practitioners != null && practitioners.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : practitioners.getEntry()) {
                Resource resource = entry.getResource();
                String nombre="";
                if (resource instanceof Practitioner) {
                    Practitioner practitioner = (Practitioner) resource;
                    nombre=practitioner.getNameFirstRep().getGiven().get(0).toString();
                    nombre+=" "+practitioner.getNameFirstRep().getFamily();// Obtiene el apellido
                    nombres.add(nombre);
                }
           }
        }
        return ResponseEntity.ok(nombres);
    }
}