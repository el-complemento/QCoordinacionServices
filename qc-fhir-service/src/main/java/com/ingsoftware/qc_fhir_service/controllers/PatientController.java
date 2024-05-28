package com.ingsoftware.qc_fhir_service.controllers;
import com.ingsoftware.qc_fhir_service.services.PatientService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PutMapping
    public ResponseEntity<String> createPatient(@RequestBody String patient) {
        String patientId = patientService.createPatient(patient);
        return ResponseEntity.ok(patientId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable String id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }
    @GetMapping("/nombre/{id}")
    public ResponseEntity<String> getPatientNombre(@PathVariable String id) {
        String nombre = "";
        Patient patient = patientService.getPatientById(id);
        nombre=patient.getNameFirstRep().getGiven().get(0).toString();
        nombre+=" "+patient.getNameFirstRep().getFamily();
        return ResponseEntity.ok(nombre);
    }
    @GetMapping
    public ResponseEntity<Bundle> getAllPatients() {
        Bundle patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
        }
    @GetMapping("/cedulas")
    public ResponseEntity<List<String>> getAllPatientsCedulas() {
        List<String> patientsCedulas = patientService.getAllPatientsCedulas();
        return ResponseEntity.ok(patientsCedulas);
    }

}


