package com.ingsoftware.qcapigatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableDiscoveryClient
public class QcApiGatewayServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QcApiGatewayServiceApplication.class, args);
	}

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		String url = "lb://qc-fhir-service";
		RouteLocator rutasFhirService = builder.routes()
				.route(r -> r.path("/api/v1/patients/**").uri(url))
				.route(r -> r.path("/api/v1/practicioners/**").uri(url))
				.route(r -> r.path("/api/v1/practitioner-roles/**").uri(url))
				.route(r -> r.path("/api/v1/service-requests/**").uri(url))
				.route(r -> r.path("/api/v1/encounters/**").uri(url))
				.route(r -> r.path("/api/v1/locations/**").uri(url))
				.build();
		return rutasFhirService;
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}