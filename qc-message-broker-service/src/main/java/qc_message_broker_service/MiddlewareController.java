package qc_message_broker_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MiddlewareController {

    private final RabbitMqPublisher rabbitMqPublisher;

    @Autowired
    public MiddlewareController(RabbitMqPublisher rabbitMqPublisher) {
        this.rabbitMqPublisher = rabbitMqPublisher;
    }

    @PostMapping("/publish")
    public void publishRequest(@RequestBody Object requestPayload) {
        String exchange = "fhir-requests-exchange";
        String routingKey = "fhir.requests.routing-key";
        rabbitMqPublisher.publish(exchange, routingKey, requestPayload);
    }
}
