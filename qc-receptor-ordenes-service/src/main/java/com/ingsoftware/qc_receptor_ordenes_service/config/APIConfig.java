package com.ingsoftware.qc_receptor_ordenes_service.config;

import io.github.cdimascio.dotenv.Dotenv;

public class APIConfig {
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure().load();

        String postgresPassword = dotenv.get("SPRING_APPLICATION_POSTGRES_PASSWORD");
        String postgresUser = dotenv.get("SPRING_APPLICATION_POSTGRES_USER");
        String fhirServiceUrl = dotenv.get("FHIR_SERVICE_URL");


        System.setProperty("SPRING_APPLICATION_POSTGRES_USER", postgresUser);
        System.setProperty("SPRING_APPLICATION_POSTGRES_PASSWORD", postgresPassword);
        System.setProperty("FHIR_SERVICE_URL", fhirServiceUrl);
    }
}