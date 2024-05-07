package com.ingsoftware.qc_receptor_ordenes_service.fhir_api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class PatientService {

    @Autowired
    private RestTemplate restTemplate;

    private final String patientBaseUrl = "http://localhost:8080/fhir/Patient/";

    public Patient getPatient(String id) throws IOException {
        String url = patientBaseUrl + "/" + id + "/_history";
        ObjectMapper objectMapper = new ObjectMapper();
        Patient lastSuccessfulPatient = null;
        int historyVersion = 1;

        while (true) {
            try {
                String requestUrl = url + "/" + historyVersion;
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl, String.class);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    String responseBody = responseEntity.getBody();
                    Patient patient = objectMapper.readValue(responseBody, Patient.class);
                    lastSuccessfulPatient = patient;
                    historyVersion++;
                } else {
                    throw new RuntimeException("Failed to fetch patient data. Status code: " + responseEntity.getStatusCodeValue());
                }
            } catch (Exception e) {
                System.err.println("Error fetching patient data: " + e.getMessage());
                return lastSuccessfulPatient;
            }
        }
    }
}
