package com.ingsoftware.qc_receptor_ordenes_service;

import com.ingsoftware.qc_receptor_ordenes_service.config.APIConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QcReceptorOrdenesServiceApplication {

	public static void main(String[] args) {
		APIConfig.loadEnv();
		SpringApplication.run(QcReceptorOrdenesServiceApplication.class, args);
	}

}
