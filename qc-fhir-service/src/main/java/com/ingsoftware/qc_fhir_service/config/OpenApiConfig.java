package com.ingsoftware.qc_fhir_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("swagger.yaml");
        if (inputStream == null) {
            throw new IOException("Unable to find swagger.yaml");
        }
        String yaml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        SwaggerParseResult parseResult = new OpenAPIV3Parser().readContents(yaml, null, null);
        if (parseResult == null || parseResult.getOpenAPI() == null) {
            throw new IOException("Unable to parse swagger.yaml");
        }
        return parseResult.getOpenAPI();
    }
}
