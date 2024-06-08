package com.ingsoftware.qcapigatewayservice.Controller;

import com.ingsoftware.qcapigatewayservice.Services.MessageSendingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class SurgeryController {

    private final MessageSendingService messageSendingService;

    @Autowired
    public SurgeryController(MessageSendingService messageSendingService) {
        this.messageSendingService = messageSendingService;
    }

    @PostMapping("/coordinate-surgery")
    public String coordinateSurgery(@RequestBody String surgeryDetails) {
        messageSendingService.sendSurgeryDetails(surgeryDetails);
        return "Request for surgery coordination has been sent.";
    }
}
