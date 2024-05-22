package com.ingsoftware.qcoordinacion_fhir_service.controllers;

import ca.uhn.fhir.rest.annotation.Search;
import org.hl7.fhir.r5.model.*;
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
<<<<<<< HEAD

    @GetMapping("/{role}")
    public ResponseEntity<Bundle> getPractitioner(@RequestBody String role){
        Bundle practitioner = practitionerRoleService.getPractitionersByRole(role);
        return ResponseEntity.ok(practitioner);
    }

    // The following method is causing compilation error, so it's commented out

    @GetMapping("/nombresRoles/{role}")
    public ResponseEntity<List<String>> getPractitionersNameWithRole(@RequestBody String role) {
=======
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
>>>>>>> develop
        Bundle practitioners = practitionerRoleService.getPractitionersByRole(role);
        List<String> cedulas = new ArrayList<>();
        if (practitioners != null && practitioners.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : practitioners.getEntry()) {
                Resource resource = entry.getResource();
<<<<<<< HEAD
                if (resource instanceof Practitioner) {
                    Practitioner practitioner = (Practitioner) resource;
                    String name = practitioner.getNameFirstRep().getFamily();
                    nombres.add(name);
=======
                if (resource instanceof Practitioner practitioner) {
                    cedulas.add(practitioner.getIdPart());
>>>>>>> develop
                }
           }
        }
<<<<<<< HEAD
        return ResponseEntity.ok(nombres);
=======
        return ResponseEntity.ok(cedulas);
>>>>>>> develop
    }
    */
}
