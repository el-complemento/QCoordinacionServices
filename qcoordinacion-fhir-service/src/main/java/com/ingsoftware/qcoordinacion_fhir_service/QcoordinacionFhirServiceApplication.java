package com.ingsoftware.qcoordinacion_fhir_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QcoordinacionFhirServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QcoordinacionFhirServiceApplication.class, args);
	}

}
