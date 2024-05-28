package com.ingsoftware.qc_receptor_ordenes_service.controllers;

import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.CreateServiceRequestDto;
import com.ingsoftware.qc_receptor_ordenes_service.services.ServiceRequestLogic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.BadRequestException;
import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/api/v1/service-requests")
public class ServiceRequestController {

    private final ServiceRequestLogic serviceRequestLogic;

    public ServiceRequestController(ServiceRequestLogic serviceRequestLogic) {
        this.serviceRequestLogic = serviceRequestLogic;
    }

    @PostMapping
    public ResponseEntity<String> CreateServiceRequest(@RequestBody CreateServiceRequestDto serviceRequestDto)
    {
        try {
            if (serviceRequestDto == null){
                throw new BadRequestException("Service data is required.");
            }
            String ID = serviceRequestLogic.add(serviceRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Service request created successfully with ID:" + ID);
        }
        catch (ValidationException e){
            throw new BadRequestException(e.getMessage());
        }
    }
}
