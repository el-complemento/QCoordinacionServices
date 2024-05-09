package com.ingsoftware.qcoordinacion_fhir_service.controllers;

import com.ingsoftware.qcoordinacion_fhir_service.services.PractitionerRoleService;
import jakarta.websocket.server.PathParam;
import org.hl7.fhir.r5.model.Bundle;
import java.net.URLDecoder;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
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
    @GetMapping("/{system}/{role}")
    public ResponseEntity<Bundle> getPractitioner(@PathVariable String system, @PathVariable String role) {
        Bundle practitioner = practitionerRoleService.getPractitionersByRole(role, system);
        return ResponseEntity.ok(practitioner);
    }
    @GetMapping("/nombresRoles/{system}/{role}")
    public ResponseEntity<List<String>> getPractitionersNameWithRole(@PathVariable String system,@PathVariable String role) {
        Bundle practitioners = practitionerRoleService.getPractitionersByRole(role,system);
        List<String> nombres = new ArrayList<>();
        if (practitioners != null && practitioners.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : practitioners.getEntry()) {
                Resource resource = entry.getResource();
                if (resource instanceof Practitioner) {
                    Practitioner practitioner = (Practitioner) resource;
                    String name = practitioner.getNameFirstRep().getFamily(); // Obtiene el apellido
                    nombres.add(name);
                }
           }
        }
        return ResponseEntity.ok(nombres);
    }
}