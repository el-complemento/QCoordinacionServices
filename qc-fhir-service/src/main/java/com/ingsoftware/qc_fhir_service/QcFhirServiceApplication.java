package com.ingsoftware.qc_fhir_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QcFhirServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QcFhirServiceApplication.class, args);
	}

}
