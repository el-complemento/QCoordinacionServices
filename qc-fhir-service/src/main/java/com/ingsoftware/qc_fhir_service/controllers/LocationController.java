package com.ingsoftware.qc_fhir_service.controllers;


import com.ingsoftware.qc_fhir_service.services.LocationService;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;
    @PutMapping
    public ResponseEntity<String> createLocation(@RequestBody String quirofano) {
        String numeroQuirofano = locationService.createQuirofano(quirofano);
        return ResponseEntity.ok(numeroQuirofano);
    }
    @GetMapping
    public ResponseEntity<String> getQuirofanosDisponibilidad() {
        JSONArray quirofanosDisponibilidad = locationService.getAllQuirofanosDisponibilidad();
        return ResponseEntity.ok(quirofanosDisponibilidad.toString());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuirofano(@PathVariable String id) {
        locationService.deleteQuirofano(id);
        return ResponseEntity.ok("DELETED");
    }
    @PostMapping("/{locationId}/updateAvailability")
    public ResponseEntity<?> updateLocationAvailability(@PathVariable String locationId,
                                                        @RequestBody UpdateAvailabilityRequest request) {
        try {
            locationService.updateLocationAvailability(locationId, request.getStart(), request.getEnd());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating location availability: " + e.getMessage());
        }
    }

    @Setter
    @Getter
    public static class UpdateAvailabilityRequest {
        private LocalDateTime start;
        private LocalDateTime end;

    }
}
