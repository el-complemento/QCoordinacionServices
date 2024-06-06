package com.ingsoftware.qcoordinacion_fhir_service;

import com.ingsoftware.qcoordinacion_fhir_service.services.PractitionerService;
import org.hl7.fhir.r5.model.Practitioner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.ingsoftware.qcoordinacion_fhir_service.controllers.PractitionerController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class PractitionerControllerTest {

    @Mock
    private PractitionerService practitionerService;

    @InjectMocks
    private PractitionerController practitionerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePractitioner() {
        when(practitionerService.createPractitioner(anyString())).thenReturn("123");

        ResponseEntity<String> response = practitionerController.createPracticioner("{}");

        assertEquals(ResponseEntity.ok("123"), response);
    }

    @Test
    public void testGetPractitioner() {
        Practitioner practitioner = new Practitioner();
        when(practitionerService.getPractitioner("123")).thenReturn(practitioner);

        ResponseEntity<Practitioner> response = practitionerController.getPractitioner("123");

        assertEquals(ResponseEntity.ok(practitioner), response);
    }

    @Test
    public void testGetPractitionerName() {
        Practitioner practitioner = new Practitioner();
        practitioner.addName().addGiven("John").setFamily("Doe");

        when(practitionerService.getPractitioner("123")).thenReturn(practitioner);

        ResponseEntity<String> response = practitionerController.getPractitionerName("123");

        assertEquals(ResponseEntity.ok("John Doe"), response);
    }
}

