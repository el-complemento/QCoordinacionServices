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
    //se mira y no se toca
    @GetMapping("/nombresRoles/{role}")
    public ResponseEntity<List<String>> getPractitionersNameWithRole(@RequestBody String role) {
        Bundle practitioners = practitionerRoleService.getPractitionersByRole(role);
        List<String> nombres = new ArrayList<>();
        if (practitioners != null && practitioners.hasEntry()) {
            // Itera sobre cada entrada del bundle
            for (Bundle.BundleEntryComponent entry : practitioners.getEntry()) {
                // Obtiene el recurso de cada entrada
                Resource resource = entry.getResource();

                // Verifica si el recurso es una instancia de Practitioner
                if (resource instanceof Practitioner) {
                    Practitioner practitioner = (Practitioner) resource;
                    // Procesa el recurso Practitioner (ejemplo: extraer nombre)
                    String name = practitioner.getNameFirstRep().getFamily(); // Obtiene el apellido
                    nombres.add(name);
                }
            }
        }
        return ResponseEntity.ok(pepe);
    }
}