package com.ingsoftware.qcoordinacion_fhir_service;

import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ingsoftware.qcoordinacion_fhir_service.controllers.PatientController;
import com.ingsoftware.qcoordinacion_fhir_service.services.PatientService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private Patient patient;
    private Bundle bundle;
    private List<String> cedulas;

    @BeforeEach
    public void setUp() {
        patient = new Patient();
        patient.setId("123");

        bundle = new Bundle();
        bundle.addEntry().setResource(patient);

        cedulas = Arrays.asList("123", "456");
    }

    @Test
    public void testCreatePatient() throws Exception {
        when(patientService.createPatient(anyString())).thenReturn("123");

        MvcResult result = mockMvc.perform(put("/api/v1/patients")
                .content("patient"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("123", result.getResponse().getContentAsString());
    }

    @Test
    public void testGetPatient() throws Exception {
        when(patientService.getPatientById("123")).thenReturn(patient);

        MvcResult result = mockMvc.perform(get("/api/v1/patients/123"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(patient, result.getResponse().getContentAsString());
    }

    @Test
    public void testGetPatientNombre() throws Exception {
        when(patientService.getPatientById("123")).thenReturn(patient);
        patient.addName().addGiven("John").setFamily("Doe");

        MvcResult result = mockMvc.perform(get("/api/v1/patients/nombre/123"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("John Doe", result.getResponse().getContentAsString());
    }

    @Test
    public void testGetAllPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(bundle);

        MvcResult result = mockMvc.perform(get("/api/v1/patients"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(bundle, result.getResponse().getContentAsString());
    }

    @Test
    public void testGetAllPatientsCedulas() throws Exception {
        when(patientService.getAllPatientsCedulas()).thenReturn(cedulas);

        MvcResult result = mockMvc.perform(get("/api/v1/patients/cedulas"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(cedulas, result.getResponse().getContentAsString());
    }
}
