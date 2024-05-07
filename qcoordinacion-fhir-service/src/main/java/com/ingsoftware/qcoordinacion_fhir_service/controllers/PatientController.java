package com.ingsoftware.qcoordinacion_fhir_service.controllers;

import com.ingsoftware.qcoordinacion_fhir_service.services.PatientService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping
    public ResponseEntity<String> createPatient(@RequestBody String patient) {
        String patientId = patientService.createPatient(patient);
        return ResponseEntity.ok("Patient created with ID: " + patientId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable String id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }
    @GetMapping("/nombre/{id}")
    public ResponseEntity<String> getPatientNombre(@PathVariable String id) {
        String patient = patientService.getPatientNombreById(id).getNameAsSingleString();
        return ResponseEntity.ok(patient);
    }

    @GetMapping
    public ResponseEntity<Bundle> getAllPatients() {
        Bundle patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
        }
}