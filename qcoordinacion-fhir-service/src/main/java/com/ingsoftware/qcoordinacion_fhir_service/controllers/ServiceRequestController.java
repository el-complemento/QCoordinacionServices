package com.ingsoftware.qcoordinacion_fhir_service.controllers;



import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.ingsoftware.qcoordinacion_fhir_service.services.ServiceRequestService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.ServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/v1/service-requests")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;


    @PostMapping
    public ResponseEntity<String> createServiceRequest(@RequestBody String serviceRequestJson) {
        String id = serviceRequestService.createServiceRequest(serviceRequestJson);
        return ResponseEntity.ok("ServiceRequest created with ID: " + id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getServiceRequest(@PathVariable String id) {
        ServiceRequest serviceRequest = serviceRequestService.getServiceRequestById(id);
        return ResponseEntity.ok(serviceRequest);
    }
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<Bundle> getServiceRequestByDate(@PathVariable String fecha) {
        Bundle serviceRequestsDeFechaHaciaAdelante = serviceRequestService.getServiceRequestsFromDate(fecha);
        return ResponseEntity.ok(serviceRequestsDeFechaHaciaAdelante);
    }
    @GetMapping("/fecha/id/{fecha}")
    public ResponseEntity<List<String>> getServiceRequestIdByDate(@PathVariable String fecha) {
        Bundle serviceRequestsDeFechaHaciaAdelante = serviceRequestService.getServiceRequestsFromDate(fecha);

        return ResponseEntity.ok((List<String>) serviceRequestsDeFechaHaciaAdelante);
    }
}


