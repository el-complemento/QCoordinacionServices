package com.ingsoftware.qc_receptor_ordenes_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ingsoftware.qc_receptor_ordenes_service.fhir_api.PatientService;
import com.ingsoftware.qc_receptor_ordenes_service.fhir_api.ServiceRequestService;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.util.Subject;
import com.ingsoftware.qc_receptor_ordenes_service.model.enums.PriorityEnum;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Patient;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.ServiceRequest;

import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.CreateServiceRequestDto;
import com.ingsoftware.qc_receptor_ordenes_service.model.enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.io.IOException;

@Service
public class ServiceRequestLogic {

    private final PatientService patientService;
    private final ServiceRequestService serviceRequestService;
    static final String CODING_SYSTEM = "SNOMED";
    static final String RESOURCE_TYPE = "ServiceRequest";
    static final String TEXTUAL_DESCRIPTION_SERVICE_REQUEST = "";


    @Autowired
    public ServiceRequestLogic(PatientService patientService, ServiceRequestService serviceRequestService) {
        this.patientService = patientService;
        this.serviceRequestService = serviceRequestService;
    }

    public String add(CreateServiceRequestDto serviceRequestDto) throws ValidationException {
        try {
            Patient patient = this.patientService.getPatient(serviceRequestDto.getPatientId());

            if (patient == null)  throw new ValidationException("Patient not found.");


            if (!isValidPriority(serviceRequestDto.getPriority())) throw new ValidationException("Invalid priority.");


            if (serviceRequestDto.getPreOperative() != null) {
                for (CreateServiceRequestDto preoperativeDto : serviceRequestDto.getPreOperative()) {
                    add(preoperativeDto);
                }
            }

            ServiceRequest parentServiceRequestEntity = createServiceRequestEntity(serviceRequestDto, patient);

            if (serviceRequestDto.getPreOperative() != null) {
                for (CreateServiceRequestDto preoperativeDto : serviceRequestDto.getPreOperative()) {
                    preoperativeDto.setBasedOn(parentServiceRequestEntity.getIdentifier());
                    add(preoperativeDto);
                }
            }
            
            return parentServiceRequestEntity.getIdentifier();
        } catch (IOException e) {
            throw new RuntimeException("Error retrieving patient data.", e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private ServiceRequest createServiceRequestEntity(CreateServiceRequestDto serviceRequestDto, Patient patient) {
       try{
        ServiceRequest serviceRequestEntity = serviceRequestDto.toEntity();
        Subject subject = new Subject();
        subject.setReference("Patient/" + serviceRequestDto.getPatientId());
        serviceRequestEntity.setSubject(subject);
        serviceRequestEntity.setResourceType(RESOURCE_TYPE);
        serviceRequestEntity.setCoding(CODING_SYSTEM, serviceRequestDto.getCode(), TEXTUAL_DESCRIPTION_SERVICE_REQUEST);
        serviceRequestEntity.setPatient(patient);
        serviceRequestEntity.setStatus(StatusEnum.ACTIVE);
        serviceRequestEntity.setPerformerType(CODING_SYSTEM,serviceRequestDto.getPerformerTypeCode(),serviceRequestDto.getPerformerTypeCode().toUpperCase());
        serviceRequestEntity.setOcurrenceTiming(serviceRequestDto.getOcurrenceTiming(),"");
        serviceRequestEntity.setRequester(serviceRequestDto.getRequester());

        final String serviceRequestID = this.serviceRequestService.createServiceRequest(serviceRequestEntity);
        if (serviceRequestID == null) {
            throw new RuntimeException("Service request creation failed.");
        }
        serviceRequestEntity.setIdentifier(serviceRequestID);

        return serviceRequestEntity;
    } catch (JsonProcessingException e){
       throw new RuntimeException("Error converting patient data.", e);}
    }

    private boolean isValidPriority(String priority) {
        for (PriorityEnum priorityEnum : PriorityEnum.values()) {
            if (priorityEnum.name().equalsIgnoreCase(priority)) {
                return true;
            }
        }
        return false;
    }
}