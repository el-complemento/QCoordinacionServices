package com.ingsoftware.qcapigatewayservice.Filters;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class PostToRabbitMQFilter extends AbstractGatewayFilterFactory<PostToRabbitMQFilter.Config> {

    private final RabbitTemplate rabbitTemplate;

    public PostToRabbitMQFilter(RabbitTemplate rabbitTemplate) {
        super(Config.class);
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (exchange.getRequest().getMethod().toString().equals("POST") &&
                    exchange.getRequest().getURI().getPath().startsWith("/api/v1/service-requests")) {

                return DataBufferUtils.join(exchange.getRequest().getBody())
                        .flatMap(dataBuffer -> {
                            byte[] bytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bytes);
                            DataBufferUtils.release(dataBuffer);

                            String body = new String(bytes);
                            rabbitTemplate.convertAndSend("surgeryExchange", "surgeryRoutingKey", body);
                            return chain.filter(exchange);
                        });
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Configuración del filtro si es necesario
    }
}
