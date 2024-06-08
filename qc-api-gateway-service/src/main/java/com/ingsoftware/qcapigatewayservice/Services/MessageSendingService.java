package com.ingsoftware.qcapigatewayservice.Services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSendingService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageSendingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSurgeryDetails(String surgeryDetails) {
        rabbitTemplate.convertAndSend("surgeryExchange", "surgeryRoutingKey", surgeryDetails);
    }
}
