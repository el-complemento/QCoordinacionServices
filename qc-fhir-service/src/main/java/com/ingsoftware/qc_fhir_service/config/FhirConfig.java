package com.ingsoftware.qc_fhir_service.config;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class FhirConfig {
    @Bean
    public FhirContext fhirContext() {
        FhirContext ctx = FhirContext.forR5();
        return ctx;
    }
    @Bean
    public IGenericClient fhirClient(FhirContext fhirContext) {
        String serverBaseUrl = "http://localhost:8080/fhir";
        return fhirContext.newRestfulGenericClient(serverBaseUrl);
    }
}
