package com.ingsoftware.qc_receptor_ordenes_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsoftware.qc_receptor_ordenes_service.fhir_api.PractitionerRoleService;
import com.ingsoftware.qc_receptor_ordenes_service.fhir_api.PractitionerService;
import com.ingsoftware.qc_receptor_ordenes_service.fhir_api.ProfileService;
import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.frontend_Dto.CreateServiceRequestDto;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Patient;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Practitioner;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.PractitionerRole;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.ServiceRequest;
import com.ingsoftware.qc_receptor_ordenes_service.model.enums.StatusEnum;
import com.ingsoftware.qc_receptor_ordenes_service.model.enums.PriorityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;

@Service
public class ServiceRequestLogic {

    private final ProfileService profileService;
    private final PractitionerService practitionerService;
    private final PractitionerRoleService practitionerRoleService;
    ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    public ServiceRequestLogic(ProfileService profileService, PractitionerService practitionerService, PractitionerRoleService practitionerRoleService) {
        this.profileService = profileService;
        this.practitionerService = practitionerService;
        this.practitionerRoleService = practitionerRoleService;
    }

    public void add(CreateServiceRequestDto serviceRequest) throws ValidationException {

        validateStatus(serviceRequest.getStatus());
        validatePriority(serviceRequest.getPriority());

        ResponseEntity<String> patientResponseEntity = profileService.getPatient(serviceRequest.getPatient());
        Patient patient = parsePatient(patientResponseEntity.getBody());
        if (patient == null) {
            throw new ValidationException("Patient not found");
        }

        ResponseEntity<String> practitionerResponseEntity = practitionerService.getPractitioner(serviceRequest.getPractitioner());
        Practitioner practitioner = parsePractitioner(practitionerResponseEntity.getBody());
        if (practitioner == null) {
            throw new ValidationException("Practitioner not found");
        }

        ResponseEntity<String> practitionerRoleResponseEntity = practitionerRoleService.getPractitionerRole(serviceRequest.getPractitionerRole());
        PractitionerRole practitionerRole = parsePractitionerRole(practitionerRoleResponseEntity.getBody());
        if (practitionerRole == null) {
            throw new ValidationException("PractitionerRole not found");
        }

        ServiceRequest serviceRequestEntity = serviceRequest.toEntity();
        serviceRequestEntity.setPatient(patient);
        //serviceRequestEntity.setPractitioner(practitioner);
        //serviceRequestEntity.setPractitionerRole(practitionerRole);

        serviceRequest.toEntity();
    }


    private void validateStatus(String status) {
        try {
            StatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status);
        }
    }

    private void validatePriority(String priority) {
        try {
            PriorityEnum.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid priority value: " + priority);
        }
    }

    private Patient parsePatient(String responseBody) {
        try {
            Patient person = objectMapper.readValue(responseBody, Patient.class);
            return person;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Practitioner parsePractitioner(String responseBody) {
        try {
            Practitioner practitioner = objectMapper.readValue(responseBody, Practitioner.class);
            return practitioner;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PractitionerRole parsePractitionerRole(String responseBody) {
        try {
            PractitionerRole practitionerRole = objectMapper.readValue(responseBody, PractitionerRole.class);
            return practitionerRole;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
