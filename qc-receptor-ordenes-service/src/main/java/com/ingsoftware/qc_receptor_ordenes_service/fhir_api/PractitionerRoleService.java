package com.ingsoftware.qc_receptor_ordenes_service.fhir_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PractitionerRoleService {

    @Autowired
    private RestTemplate restTemplate;
    private final String practitionerRoleBaseUrl = "http://localhost:8181/api/v1/practitioner-roles";


    public ResponseEntity<String> createPractitionerRole(String practitionerRole) {
        String url = practitionerRoleBaseUrl;
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, practitionerRole, String.class);
        return responseEntity;
    }

    public ResponseEntity<String> getPractitionerRole(String id) {
        String url = practitionerRoleBaseUrl + "/" + id;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity;
    }

    public ResponseEntity<String> getPractitionersByRole(String role) {
        String url = practitionerRoleBaseUrl + "/" + role;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity;
    }
}
