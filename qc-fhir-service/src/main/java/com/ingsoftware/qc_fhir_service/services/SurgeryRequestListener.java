package com.ingsoftware.qc_fhir_service.services;

import com.ingsoftware.qc_fhir_service.services.ServiceRequestService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurgeryRequestListener {

    private final ServiceRequestService surgeryService;

    @Autowired
    public SurgeryRequestListener(ServiceRequestService surgeryService) {
        this.surgeryService = surgeryService;
    }

    @RabbitListener(queues = "surgeryQueue")
    public void receiveMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);
        surgeryService.createServiceRequest(message);
    }
}
