package com.ingsoftware.qc_fhir_service.controllers;

import ca.uhn.fhir.rest.annotation.Search;
import com.ingsoftware.qc_fhir_service.services.PractitionerRoleService;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r5.model.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/practitioner-roles")
public class PractitionerRoleController {
        @Autowired
        private PractitionerRoleService practitionerRoleService;

        @GetMapping
        public ResponseEntity<String> getDisponiblidades() {
            JSONArray getDisponibilidades = practitionerRoleService.getDisponibilidadesRoles();
            return ResponseEntity.ok(getDisponibilidades.toString());
        }
        @PostMapping
        public ResponseEntity<String> createPractitionerRole(@RequestBody String practitioner) {
            String practitionerRoleId = practitionerRoleService.createPractitionerRole(practitioner);
            return ResponseEntity.ok(": " + practitionerRoleId);
        }
        @DeleteMapping("/{id}")
        public ResponseEntity<String> deleteRole(@PathVariable String id) {
            practitionerRoleService.deleteRoleById(id);
            return ResponseEntity.ok("DELETED");
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
        @PostMapping("/{practitionerId}/updateAvailability")
        public ResponseEntity<?> updateMedicoAvailability(@PathVariable String practitionerId,
                                                        @RequestBody PractitionerRoleController.UpdateAvailabilityRequest request) {
            try {
                practitionerRoleService.updatePractitionerRoleAvailability(practitionerId, request.getStart(), request.getEnd());
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error updating location availability: " + e.getMessage());
            }
        }


        @Setter
        @Getter
        public static class UpdateAvailabilityRequest {
            private LocalDateTime start;
            private LocalDateTime end;
        }
}