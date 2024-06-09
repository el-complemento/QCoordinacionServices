package com.ingsoftware.qcapigatewayservice;

import com.ingsoftware.qcapigatewayservice.Filters.PostToRabbitMQFilter;
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
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder,PostToRabbitMQFilter postToRabbitMQFilter) {
		String urlFhirBackend = "lb://qc-fhir-service";
		String urlALgoritmo = "lb://qc-algoritmo";
		return builder.routes()
				.route(r -> r.path("/api/v1/patients/**")
						.filters(f -> f.filter(postToRabbitMQFilter.apply(new PostToRabbitMQFilter.Config())))
						.uri(urlFhirBackend))
				.route(r -> r.path("/api/v1/practicioners/**").uri(urlFhirBackend))
				.route(r -> r.path("/api/v1/practitioner-roles/**").uri(urlFhirBackend))
				.route(r -> r.path("/api/v1/service-requests/**").uri(urlFhirBackend))
				.route(r -> r.path("/api/v1/encounters/**").uri(urlFhirBackend))
				.route(r -> r.path("/api/v1/locations/**").uri(urlFhirBackend))
				.route(r -> r.path("/api/v1/appointments/**").uri(urlFhirBackend))
				.route(r -> r.path("/api/v1/algoritmo/**").uri(urlALgoritmo))
				.build();
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