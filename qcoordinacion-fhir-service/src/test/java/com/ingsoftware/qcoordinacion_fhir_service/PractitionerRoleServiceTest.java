package com.ingsoftware.qcoordinacion_fhir_service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.ICriterion;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ingsoftware.qcoordinacion_fhir_service.services.PractitionerRoleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.DayOfWeek;

public class PractitionerRoleServiceTest {

    @Mock
    private IGenericClient fhirClient;

    @InjectMocks
    private PractitionerRoleService practitionerRoleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePractitionerRole() {
        PractitionerRole practitionerRole = new PractitionerRole();
        practitionerRole.setId("123");
        
        // Create an instance of IIdType with the ID string "123"
        IIdType idType = new IdType("123");
        
        // Use the created IIdType instance to set the ID
        MethodOutcome methodOutcome = new MethodOutcome();
        methodOutcome.setId(idType);

        when(fhirClient.create().resource(any(PractitionerRole.class)).execute()).thenReturn(methodOutcome);

        String createdId = practitionerRoleService.createPractitionerRole("{}");

        assertEquals("123", createdId);
    }

    @Test
    public void testGetPractitionerRole() {
        PractitionerRole practitionerRole = new PractitionerRole();
        Bundle bundle = new Bundle().addEntry(new Bundle.BundleEntryComponent().setResource(practitionerRole));

        // Explicitly specify the type of ICriterion being passed to the where() method
        ICriterion<?> criterion = (ICriterion<?>) any();

        // Use the explicitly specified type in the where() method call
        when(fhirClient.search().forResource(PractitionerRole.class).where(criterion).returnBundle(Bundle.class).execute()).thenReturn(bundle);

        PractitionerRole retrievedPractitionerRole = practitionerRoleService.getPractitionerRole("123");

        assertEquals(practitionerRole, retrievedPractitionerRole);
    }


    /*
    @Test
    public void testGetPractitionerDisponibilidad() {
        PractitionerRole practitionerRole = new PractitionerRole();
        Availability availability = new Availability();
        availability.addAvailableTime(DayOfWeek.MONDAY);
        practitionerRole.addAvailability(availability);
        Bundle bundle = new Bundle().addEntry(new Bundle.BundleEntryComponent().setResource(practitionerRole));
        when(fhirClient.search().forResource(PractitionerRole.class).where(any()).returnBundle(Bundle.class).execute()).thenReturn(bundle);

        String disponibilidad = practitionerRoleService.getPractitionerDisponibilidad("123");

        // You may need to customize the assertion based on the actual encoding logic
        assertEquals("{\"daysOfWeek\":[\"mon\"]}", disponibilidad);
    }
    */

   /* @Test
    public void testGetPractitionersByRole() {
        Bundle bundle = new Bundle();
        when(fhirClient.search().forResource(PractitionerRole.class)
        .where(SearchParam.class.cast(any()).eq("parameterName", "parameterValue"))
        .include(any())
        .returnBundle(Bundle.class)
        .execute()).thenReturn(bundle);


        Bundle retrievedBundle = practitionerRoleService.getPractitionersByRole("roleCode");

        assertEquals(bundle, retrievedBundle);
    }
    */
}
