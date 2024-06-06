package com.ingsoftware.qcoordinacion_fhir_service;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ICreateTyped;
import ca.uhn.fhir.rest.gclient.IUpdateTyped;
import ca.uhn.fhir.rest.gclient.IReadExecutable;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.ICriterion;
import com.ingsoftware.qcoordinacion_fhir_service.services.ServiceRequestService;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.ServiceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ServiceRequestServiceTest {

    @Mock
    private IGenericClient fhirClient;

    @InjectMocks
    private ServiceRequestService serviceRequestService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateServiceRequest() {
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(new org.hl7.fhir.r5.model.IdType("123"));

        ICreateTyped createTyped = mock(ICreateTyped.class);
        when(fhirClient.create().resource(any(ServiceRequest.class))).thenReturn(createTyped);
        when(createTyped.execute()).thenReturn(methodOutcome);

        String outcome = serviceRequestService.createServiceRequest("{}");

        verify(fhirClient.create()).resource(any(ServiceRequest.class));
        verify(createTyped).execute();
        assertEquals("123", outcome);
    }

    @Test
    public void testGetServiceRequestById() {
        ServiceRequest serviceRequest = new ServiceRequest();
        IReadExecutable<ServiceRequest> readExecutable = mock(IReadExecutable.class);
        when(fhirClient.read().resource(ServiceRequest.class).withId("123")).thenReturn(readExecutable);
        when(readExecutable.execute()).thenReturn(serviceRequest);

        ServiceRequest result = serviceRequestService.getServiceRequestById("123");

        verify(fhirClient.read().resource(ServiceRequest.class).withId("123")).execute();
        assertEquals(serviceRequest, result);
    }

    @Test
    public void testGetServiceRequestsFromDate() {
        Bundle bundle = new Bundle();
        IQuery<IQuery> query = mock(IQuery.class);
        IQuery<Bundle> queryBundle = mock(IQuery.class);

        when(fhirClient.search().forResource(ServiceRequest.class)).thenReturn(query.execute());
        when(query.where(any(ICriterion.class))).thenReturn(query);
        when(query.returnBundle(Bundle.class)).thenReturn(queryBundle);
        when(queryBundle.execute()).thenReturn(bundle);

        Bundle result = serviceRequestService.getServiceRequestsFromDate("2024-05-28");

        verify(fhirClient.search().forResource(ServiceRequest.class)).where(any(ICriterion.class)).returnBundle(Bundle.class).execute();
        assertEquals(bundle, result);
    }

    @Test
    public void testMarkServiceRequestAsCompleted() {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setBasedOn(Collections.singletonList(new org.hl7.fhir.r5.model.Reference().setReference("123")));
        MethodOutcome methodOutcome = new MethodOutcome();

        IReadExecutable<ServiceRequest> readExecutable = mock(IReadExecutable.class);
        when(fhirClient.read().resource(ServiceRequest.class).withId("123")).thenReturn(readExecutable);
        when(readExecutable.execute()).thenReturn(serviceRequest);

        IUpdateTyped updateTyped = mock(IUpdateTyped.class);
        when(fhirClient.update().resource(any(ServiceRequest.class))).thenReturn(updateTyped);
        when(updateTyped.execute()).thenReturn(methodOutcome);

        ServiceRequest result = serviceRequestService.markServiceRequestAsCompleted("123");

        verify(fhirClient.read().resource(ServiceRequest.class).withId("123")).execute();
        verify(fhirClient.update().resource(any(ServiceRequest.class)).execute());
        assertEquals(serviceRequest, result);
    }
}
