package qc_message_broker_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqPublisher {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMqPublisher.class);

	private final AmqpTemplate amqpTemplate;

	@Autowired
	public RabbitMqPublisher(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}

	public void publish(String exchange, String routingKey, Object payload) {
		amqpTemplate.convertAndSend(exchange, routingKey, payload);
		logger.info("Publishing to {} using routing key {}. Payload: {}", exchange, routingKey, payload);
	}
}
