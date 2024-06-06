package com.ingsoftware.qcoordinacion_fhir_service;

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IDelete;
import ca.uhn.fhir.rest.gclient.IDeleteTyped;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.ingsoftware.qcoordinacion_fhir_service.services.EncounterService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class EncounterServiceTest {

    @Mock
    private IGenericClient fhirClient;

    @InjectMocks
    private EncounterService encounterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateEncounter() {
        // Mocking
        Encounter encounter = new Encounter();
        encounter.setId("123");
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(new IdType("Encounter", encounter.getId()));
        when(fhirClient.create().resource(any(Encounter.class)).execute()).thenReturn(methodOutcome);

        // Test
        String createdId = encounterService.createEncounter("{}");

        // Assertion
        assertEquals("123", createdId);
    }

    @Test
    public void testGetEncounterById() {
        // Mocking
        Encounter encounter = new Encounter();
        when(fhirClient.read().resource(Encounter.class).withId("123").execute()).thenReturn(encounter);

        // Test
        Encounter retrievedEncounter = encounterService.getEncounterbyId("123");

        // Assertion
        assertEquals(encounter, retrievedEncounter);
    }

    @Test
    public void testSetEncounterAsCompleted() {
        // Mocking
        Encounter encounter = new Encounter();
        when(fhirClient.read().resource(Encounter.class).withId("123").execute()).thenReturn(encounter);

        // Test
        encounterService.setEncounterAsCompleted("123");

        // Assertion
        assertEquals(Enumerations.EncounterStatus.COMPLETED, encounter.getStatus());
    }

    @Test
    public void testDeleteEncounterById() {
        // Mocking
        IDelete deleteMock = mock(IDelete.class);
        IDeleteTyped deleteTypedMock = mock(IDeleteTyped.class);
        when(fhirClient.delete()).thenReturn(deleteMock);
        when(deleteMock.resourceById(any(IdType.class))).thenReturn(deleteTypedMock);
        when(deleteTypedMock.execute()).thenReturn(new MethodOutcome());

        // Test
        encounterService.deleteEncounterById("123");

        // Assertion
        // Verify that fhirClient.delete().resourceById() method is called with any IdType argument
        verify(fhirClient).delete().resourceById(any(IdType.class)).execute();
    }

    @Test
    public void testDevolverDataLinda() {
        // Mock Data
        Bundle bundle = new Bundle();
        List<Bundle.BundleEntryComponent> entries = new ArrayList<>();
        Bundle.BundleEntryComponent entry1 = new Bundle.BundleEntryComponent();
        Encounter encounter = new Encounter();
        encounter.setId("123");
        encounter.setPlannedStartDate(new Date());
        encounter.setPlannedEndDate(new Date());
        encounter.setPriority(new CodeableConcept().addCoding(new Coding().setDisplay("High")));
        encounter.setSubject(new Reference().setReference("Patient/1"));
        encounter.addParticipant(new Encounter.EncounterParticipantComponent().setActor(new Reference().setReference("Practitioner/1")));
        entry1.setResource(encounter);
        entries.add(entry1);
        bundle.setEntry(entries);

        // Mocking
        when(fhirClient.search().forResource(Encounter.class).returnBundle(Bundle.class).execute()).thenReturn(bundle);
        ServiceRequest serviceRequest = new ServiceRequest();
        CodeableReference codeableReference = new CodeableReference().setConcept(new CodeableConcept().setText("Surgery"));
        serviceRequest.setCode(codeableReference);
        when(fhirClient.read().resource(ServiceRequest.class).withId(anyString()).execute()).thenReturn(serviceRequest);
        Practitioner practitioner = new Practitioner();
        practitioner.addName().addGiven("John").setFamily("Doe");
        when(fhirClient.read().resource(Practitioner.class).withId(anyString()).execute()).thenReturn(practitioner);
        Patient patient = new Patient();
        patient.addName().addGiven("Alice").setFamily("Smith");
        when(fhirClient.read().resource(Patient.class).withId(anyString()).execute()).thenReturn(patient);

        // Test
        List<String> data = encounterService.devolverDataLinda();

        // Assertions
        assertEquals(1, data.size());
    }
}
