package com.ingsoftware.qcoordinacion_fhir_service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ingsoftware.qcoordinacion_fhir_service.services.PatientService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PatientServiceTest {

    @Mock
    private IGenericClient fhirClient;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePatient() {
        Patient patient = new Patient();
        patient.setId("123");
        String patientJson = "{}"; // This can be a valid JSON string representing a Patient object

        // Create an IIdType object from the patient ID
        IIdType idType = new IdType(patient.getId());

        // Stub the behavior to return a MethodOutcome with the created ID
        when(fhirClient.update().resource(any(Patient.class)).execute()).thenReturn(new MethodOutcome().setId(idType));

        String createdPatientId = patientService.createPatient(patientJson);

        assertEquals("123", createdPatientId);
    }


    @Test
    public void testGetPatientNombreById() {
        HumanName humanName = new HumanName();
        humanName.addGiven("John").setFamily("Doe");

        Patient patient = new Patient();
        patient.addName(humanName);

        when(fhirClient.read().resource(Patient.class).withId("123").execute()).thenReturn(patient);

        HumanName retrievedName = patientService.getPatientNombreById("123");

        assertEquals("John", retrievedName.getGivenAsSingleString());
        assertEquals("Doe", retrievedName.getFamily());
    }

    @Test
    public void testGetPatientById() {
        Patient patient = new Patient();
        patient.setId("123");

        when(fhirClient.read().resource(Patient.class).withId("123").execute()).thenReturn(patient);

        Patient retrievedPatient = patientService.getPatientById("123");

        assertEquals("123", retrievedPatient.getId());
    }

    @Test
    public void testGetAllPatients() {
        Bundle bundle = new Bundle();

        when(fhirClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute()).thenReturn(bundle);

        Bundle retrievedBundle = patientService.getAllPatients();

        assertEquals(bundle, retrievedBundle);
    }

    @Test
    public void testGetAllPatientsCedulas() {
        Bundle bundle = new Bundle();
        Bundle.BundleEntryComponent entry1 = new Bundle.BundleEntryComponent();
        entry1.setResource(new Patient().setId("123"));
        Bundle.BundleEntryComponent entry2 = new Bundle.BundleEntryComponent();
        entry2.setResource(new Patient().setId("456"));
        bundle.addEntry(entry1).addEntry(entry2);

        List<String> expectedCedulas = List.of("123", "456");

        when(fhirClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute()).thenReturn(bundle);

        List<String> retrievedCedulas = patientService.getAllPatientsCedulas();

        assertEquals(expectedCedulas, retrievedCedulas);
    }
}
