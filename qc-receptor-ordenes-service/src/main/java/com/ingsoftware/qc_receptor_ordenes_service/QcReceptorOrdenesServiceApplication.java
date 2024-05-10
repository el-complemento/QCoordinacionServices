package com.ingsoftware.qc_receptor_ordenes_service;

import com.ingsoftware.qc_receptor_ordenes_service.config.APIConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class QcReceptorOrdenesServiceApplication {

	public static void main(String[] args) {
		APIConfig.loadEnv();
		SpringApplication.run(QcReceptorOrdenesServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
