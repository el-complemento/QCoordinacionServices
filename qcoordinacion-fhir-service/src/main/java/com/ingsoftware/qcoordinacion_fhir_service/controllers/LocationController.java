package com.ingsoftware.qcoordinacion_fhir_service.controllers;

import com.ingsoftware.qcoordinacion_fhir_service.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;
    @PutMapping
    public ResponseEntity<String> creatLocation(@RequestBody String quirofano) {
        String numeroQuirofano = locationService.createQuirofano(quirofano);
        return ResponseEntity.ok(numeroQuirofano);
    }

    @GetMapping("/disponibilidad/{id}")
    public ResponseEntity<String> getDisponibilidadQuirofano(@RequestBody String idQuirofano) {
        String disponibilidadQuirofano = locationService.getDisponibilidadQuirofano(idQuirofano);
        return ResponseEntity.ok(disponibilidadQuirofano);
    }
    @GetMapping("/cantidad")
    public ResponseEntity<Integer> getCantidadQuirofanos() {
        return ResponseEntity.ok(locationService.getCantidadQuirofanos());
    }

}
