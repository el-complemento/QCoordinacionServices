package com.ingsoftware.qcapigatewayservice.Filters;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PostToRabbitMQFilter extends AbstractGatewayFilterFactory<PostToRabbitMQFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(PostToRabbitMQFilter.class);
    private final RabbitTemplate rabbitTemplate;

    public PostToRabbitMQFilter(RabbitTemplate rabbitTemplate) {
        super(Config.class);
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if ("PUT".equals(exchange.getRequest().getMethod().toString()) &&
                    exchange.getRequest().getURI().getPath().startsWith("/api/v1/patients")) {

                logger.info("Processing PUT request to /api/v1/patients");

                return DataBufferUtils.join(exchange.getRequest().getBody())
                        .flatMap(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);

                            String body = new String(bytes);
                            logger.info("Sending message to RabbitMQ: {}", body);

                            // Send the message asynchronously and handle exceptions
                            Mono.fromRunnable(() -> {
                                try {
                                    rabbitTemplate.convertAndSend("surgeryExchange", "surgeryRoutingKey", body);
                                    logger.info("Message sent to RabbitMQ successfully");
                                } catch (Exception e) {
                                    logger.error("Failed to send message to RabbitMQ: {}", e.getMessage());
                                }
                            }).subscribe();

                            // Continue the filter chain
                            return chain.filter(exchange);
                        })
                        .onErrorResume(e -> {
                            logger.error("Error processing request: {}", e.getMessage(), e);
                            return Mono.error(e);
                        });
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuración del filtro si es necesario
    }
}