package com.ingsoftware.qc_fhir_service.controllers;

import com.ingsoftware.qc_fhir_service.services.ServiceRequestService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.ServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/service-requests")
public class ServiceRequestController {

    @Autowired
    private ServiceRequestService serviceRequestService;

    @GetMapping
    public ResponseEntity<String> getServiceRequestsWithoutBasedOn() {
        JSONArray ids = serviceRequestService.fetchServiceRequestsWithBasedOnNull();
        return ResponseEntity.ok(ids.toString());
    }

    @PostMapping
    public ResponseEntity<String> createServiceRequest(@RequestBody String serviceRequestJson) {
        String id = serviceRequestService.createServiceRequest(serviceRequestJson);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getServiceRequest(@PathVariable String id) {
        ServiceRequest serviceRequest = serviceRequestService.getServiceRequestById(id);
        return ResponseEntity.ok(serviceRequest);
    }

    @GetMapping("id/fecha/{id}")
    public ResponseEntity<Date> getFechaServiceRequest(@PathVariable String id) {
        ServiceRequest serviceRequest = serviceRequestService.getServiceRequestById(id);
        Date fechaOrden = serviceRequest.getAuthoredOn();
        return ResponseEntity.ok(fechaOrden);
    }

    @GetMapping("/patientId/{id}")
    public ResponseEntity<String> getServiceRequestPatientId(@PathVariable String id) {
        ServiceRequest serviceRequest = serviceRequestService.getServiceRequestById(id);
        String idPaciente = serviceRequest.getSubject().getReference();
        return ResponseEntity.ok(idPaciente);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> getServiceRequestStatus(@PathVariable String id) {
        String serviceRequest = serviceRequestService.getServiceRequestById(id).getStatus().toString();
        return ResponseEntity.ok(serviceRequest);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<Bundle> getServiceRequestByDate(@PathVariable String fecha) {
        Bundle serviceRequestsDeFechaHaciaAdelante = serviceRequestService.getServiceRequestsFromDate(fecha);
        return ResponseEntity.ok(serviceRequestsDeFechaHaciaAdelante);
    }
    @GetMapping("/active/no-based-on")
    public ResponseEntity<String> getActiveServiceRequestsWithoutBasedOn() {
        JSONArray ids = serviceRequestService.fetchActiveServiceRequestsWithBasedOnNull();
        return ResponseEntity.ok(ids.toString());
    }

    @GetMapping("/fecha/id/{fecha}")
    public ResponseEntity<List<String>> getServiceRequestIdByDate(@PathVariable String fecha) {
        Bundle serviceRequestsDeFechaHaciaAdelante = serviceRequestService.getServiceRequestsFromDate(fecha);
        List<String> idsOrdenes = new ArrayList<>();
        if (serviceRequestsDeFechaHaciaAdelante != null && serviceRequestsDeFechaHaciaAdelante.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : serviceRequestsDeFechaHaciaAdelante.getEntry()) {
                Resource resource = entry.getResource();
                if (resource instanceof ServiceRequest) {
                    idsOrdenes.add(resource.getIdPart());
                }
            }
        }
        return ResponseEntity.ok(idsOrdenes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> markServiceRequestAsCompleted(@PathVariable String id) {
        ServiceRequest serviceRequestFunciono = serviceRequestService.markServiceRequestAsCompleted(id);
        return ResponseEntity.ok(serviceRequestFunciono.toString());
    }
}