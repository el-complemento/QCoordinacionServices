package com.ingsoftware.qcoordinacion_fhir_service;

import com.ingsoftware.qcoordinacion_fhir_service.services.ServiceRequestService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ingsoftware.qcoordinacion_fhir_service.controllers.ServiceRequestController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class ServiceRequestControllerTest {

    @Mock
    private ServiceRequestService serviceRequestService;

    @InjectMocks
    private ServiceRequestController serviceRequestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateServiceRequest() {
        when(serviceRequestService.createServiceRequest(anyString())).thenReturn("123");

        ResponseEntity<String> response = serviceRequestController.createServiceRequest("{}");

        assertEquals(ResponseEntity.ok("123"), response);
    }

    @Test
    public void testGetServiceRequest() {
        ServiceRequest serviceRequest = new ServiceRequest();
        when(serviceRequestService.getServiceRequestById("123")).thenReturn(serviceRequest);

        ResponseEntity<ServiceRequest> response = serviceRequestController.getServiceRequest("123");

        assertEquals(ResponseEntity.ok(serviceRequest), response);
    }

    @Test
    public void testGetFechaServiceRequest() {
        Date date = new Date();
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setAuthoredOn(date);
        when(serviceRequestService.getServiceRequestById("123")).thenReturn(serviceRequest);

        ResponseEntity<Date> response = serviceRequestController.getFechaServiceRequest("123");

        assertEquals(ResponseEntity.ok(date), response);
    }

    @Test
    public void testGetServiceRequestPatientId() {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.getSubject().setReference("patientId");
        when(serviceRequestService.getServiceRequestById("123")).thenReturn(serviceRequest);

        ResponseEntity<String> response = serviceRequestController.getServiceRequestPatientId("123");

        assertEquals(ResponseEntity.ok("patientId"), response);
    }

    @Test
    public void testGetServiceRequestStatus() {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setStatus(Enumerations.RequestStatus.ACTIVE);
        when(serviceRequestService.getServiceRequestById("123")).thenReturn(serviceRequest);

        ResponseEntity<String> response = serviceRequestController.getServiceRequestStatus("123");

        assertEquals(ResponseEntity.ok("ACTIVE"), response);
    }

    @Test
    public void testGetServiceRequestByDate() {
        Bundle bundle = new Bundle();
        when(serviceRequestService.getServiceRequestsFromDate("fecha")).thenReturn(bundle);

        ResponseEntity<Bundle> response = serviceRequestController.getServiceRequestByDate("fecha");

        assertEquals(ResponseEntity.ok(bundle), response);
    }

    @Test
    public void testGetServiceRequestIdByDate() {
        Bundle bundle = new Bundle();
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setId("123");
        bundle.addEntry().setResource(serviceRequest);

        when(serviceRequestService.getServiceRequestsFromDate("fecha")).thenReturn(bundle);

        ResponseEntity<List<String>> response = serviceRequestController.getServiceRequestIdByDate("fecha");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("123");

        assertEquals(ResponseEntity.ok(expectedIds), response);
    }

    @Test
    public void testMarkServiceRequestAsCompleted() {
        ServiceRequest serviceRequest = new ServiceRequest();
        when(serviceRequestService.markServiceRequestAsCompleted("123")).thenReturn(serviceRequest);

        ResponseEntity<ServiceRequest> response = serviceRequestController.markServiceRequestAsCompleted("123");

        assertEquals(ResponseEntity.ok(serviceRequest), response);
    }
}
