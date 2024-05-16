package com.ingsoftware.qc_receptor_ordenes_service.fhir_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PractitionerService {

    @Autowired
    private RestTemplate restTemplate;

    private final String practitionerBaseUrl = "http://localhost:8181/api/v1/practicioners";

    public ResponseEntity<String> createPractitioner(String practitioner) {
        String url = practitionerBaseUrl;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, practitioner, String.class);
        return responseEntity;
    }

    public ResponseEntity<String> getPractitioner(String id) {
        String url = practitionerBaseUrl + "/" + id;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity;
    }
}
