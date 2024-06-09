package com.ingsoftware.qc_fhir_service.services;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurgeryRequestListener {

    private final PatientService patientService;

    @Autowired
    public SurgeryRequestListener(PatientService patientService) {
        this.patientService = patientService;
    }

    @RabbitListener(queues = "surgeryQueue")
    public void receiveMessage(String message) {
        System.out.println("Received message from RabbitMQ: " + message);
        patientService.createPatient(message);
    }
}
