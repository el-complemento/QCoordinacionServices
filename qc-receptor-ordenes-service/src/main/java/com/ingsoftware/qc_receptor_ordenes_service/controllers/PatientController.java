package com.ingsoftware.qc_receptor_ordenes_service.controllers;

import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.CreatePatientDto;
import com.ingsoftware.qc_receptor_ordenes_service.services.PatientLogic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientLogic patientLogic;

    public PatientController(PatientLogic patientLogic) {
        this.patientLogic = patientLogic;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreatePatientDto> getPatientById(@PathVariable String id) { // Add @PathVariable annotation
        CreatePatientDto patientDto = new CreatePatientDto();
        patientDto = patientDto.fromEntity(patientLogic.getPatientById(id)) ;

        return new ResponseEntity<>(patientDto, HttpStatus.OK);
    }
}