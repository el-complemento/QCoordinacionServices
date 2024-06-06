package com.ingsoftware.qcoordinacion_fhir_service;

import com.ingsoftware.qcoordinacion_fhir_service.services.EncounterService;
import org.hl7.fhir.r5.model.Encounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import com.ingsoftware.qcoordinacion_fhir_service.controllers.EncounterController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EncounterControllerTest {

    @Mock
    private EncounterService encounterService;

    @InjectMocks
    private EncounterController encounterController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateEncounter() {
        when(encounterService.createEncounter(anyString())).thenReturn("123");

        ResponseEntity<String> response = encounterController.createEncounter("{}");

        assertEquals(ResponseEntity.ok("123"), response);
    }

    @Test
    public void testSetEncounterAsCompleted() {
        ResponseEntity<String> response = encounterController.setEncounterAsCompleted("123");

        assertEquals(ResponseEntity.ok("Se cambio correctamente el estado"), response);
        verify(encounterService).setEncounterAsCompleted("123");
    }

    @Test
    public void testGetEncounter() {
        Encounter encounter = new Encounter();
        when(encounterService.getEncounterbyId("123")).thenReturn(encounter);

        ResponseEntity<Encounter> response = encounterController.getEncounter("123");

        assertEquals(ResponseEntity.ok(encounter), response);
    }

    @Test
    public void testGetAllEncountersDataLinda() {
        List<String> data = List.of("data1", "data2");
        when(encounterService.devolverDataLinda()).thenReturn(data);

        ResponseEntity<List<String>> response = encounterController.getAllEncountersDataLinda();

        assertEquals(ResponseEntity.ok(data), response);
    }

    @Test
    public void testDeleteEncounter() {
        ResponseEntity<String> response = encounterController.deleteEncounter("123");

        assertEquals(ResponseEntity.ok("DELETED"), response);
        verify(encounterService).deleteEncounterById("123");
    }
}
