package com.ingsoftware.qc_receptor_ordenes_service.controllers;

import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.frontend_Dto.CreateServiceRequestDto;
import com.ingsoftware.qc_receptor_ordenes_service.services.ServiceRequestLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/service-requests")
public class ServiceRequestController {

    private final ServiceRequestLogic serviceRequestLogic;

    @Autowired
    public ServiceRequestController(ServiceRequestLogic serviceRequestLogic) {
        this.serviceRequestLogic = serviceRequestLogic;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createServiceRequest(@RequestBody CreateServiceRequestDto createServiceRequestDto) {
        try {
            serviceRequestLogic.add(createServiceRequestDto);
            return new ResponseEntity<>("ServiceRequest created successfully", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ValidationException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}