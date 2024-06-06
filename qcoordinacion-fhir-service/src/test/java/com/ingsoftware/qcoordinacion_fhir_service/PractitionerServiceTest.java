package com.ingsoftware.qcoordinacion_fhir_service;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r5.model.HumanName;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ingsoftware.qcoordinacion_fhir_service.services.PractitionerService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PractitionerServiceTest {

    @Mock
    private IGenericClient fhirClient;

    @InjectMocks
    private PractitionerService practitionerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePractitioner() {
        Practitioner practitioner = new Practitioner();
        practitioner.setId("123");
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(new IdType("Practitioner", practitioner.getId()));

        when(fhirClient.update().resource(any(Practitioner.class)).execute()).thenReturn(methodOutcome);

        String createdId = practitionerService.createPractitioner("{}");

        assertEquals("123", createdId);
    }

    @Test
    public void testGetPractitioner() {
        Practitioner practitioner = new Practitioner();
        when(fhirClient.read().resource(Practitioner.class).withId("123").execute()).thenReturn(practitioner);

        Practitioner retrievedPractitioner = practitionerService.getPractitioner("123");

        assertEquals(practitioner, retrievedPractitioner);
    }

    @Test
    public void testGetPractitionerNombreById() {
        HumanName humanName = new HumanName();
        humanName.addGiven("John").setFamily("Doe");

        Practitioner practitioner = new Practitioner();
        practitioner.addName(humanName);

        when(fhirClient.read().resource(Practitioner.class).withId("123").execute()).thenReturn(practitioner);

        HumanName retrievedName = practitionerService.getPractitionerNombreById("123");

        assertEquals("John", retrievedName.getGivenAsSingleString());
        assertEquals("Doe", retrievedName.getFamily());
    }
}
