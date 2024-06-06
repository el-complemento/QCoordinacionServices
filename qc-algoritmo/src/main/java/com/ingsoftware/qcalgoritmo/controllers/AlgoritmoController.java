package com.ingsoftware.qcalgoritmo.controllers;

import com.ingsoftware.qcalgoritmo.services.AlgoritmoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/algoritmo")
public class AlgoritmoController {
    @Autowired
    private AlgoritmoService algoritmoService;

    @GetMapping
    public ResponseEntity<String> algoritmoTime(){
        algoritmoService.processRequests();
        return ResponseEntity.ok("True");
    }
}
