package com.ingsoftware.qc_receptor_ordenes_service.fhir_api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.ServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ServiceRequestService {

    private RestTemplate restTemplate;

    private final String url = "http://localhost:8082/api/v1/service-requests";

    @Autowired
    public ServiceRequestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String createServiceRequest(ServiceRequest serviceRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(serviceRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();
        if (!statusCode.is2xxSuccessful()) {
            return null;
        }

        String responseBody = responseEntity.getBody();
        if (responseBody != null) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(responseBody);
            if (matcher.find()) {
                return matcher.group();
            }
        }
        return null;
    }
}