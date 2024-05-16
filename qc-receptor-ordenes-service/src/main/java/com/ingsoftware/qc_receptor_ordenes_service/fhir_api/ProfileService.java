package com.ingsoftware.qc_receptor_ordenes_service.fhir_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProfileService {

    @Autowired
    private RestTemplate restTemplate;

    private final String patientBaseUrl = "http://localhost:8181/api/v1/patients";

    public ResponseEntity<String> getPatient(String id) {
        String url = patientBaseUrl + "/" + id;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity;
    }
}
