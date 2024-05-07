package com.ingsoftware.qc_receptor_ordenes_service.services;

import com.ingsoftware.qc_receptor_ordenes_service.fhir_api.PatientService;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Patient;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PatientLogic {

    private PatientService  patientService;

    PatientLogic(PatientService patientService) {
        this.patientService = patientService;
    }

    public Patient getPatientById(String id) {
        Patient patient = null;
        try {
            patient = patientService.getPatient(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return patient;
    }
}