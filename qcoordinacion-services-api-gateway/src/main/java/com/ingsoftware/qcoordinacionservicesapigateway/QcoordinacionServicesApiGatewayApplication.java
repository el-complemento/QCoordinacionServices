package com.ingsoftware.qcoordinacionservicesapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class QcoordinacionServicesApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(QcoordinacionServicesApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		String url = "lb://qcoordinacion-fhir-service";
		RouteLocator rutasFhirService = builder.routes()
				.route(r -> r.path("/api/v1/patients/**").uri(url))
				.route(r -> r.path("/api/v1/practicioners/**").uri(url))
				.route(r -> r.path("/api/v1/practitioner-roles/**").uri(url))
				.route(r -> r.path("/api/v1/service-requests/**").uri(url))
				.route(r -> r.path("/api/v1/encounters/**").uri(url))
				.build();
		return rutasFhirService;
	}
}