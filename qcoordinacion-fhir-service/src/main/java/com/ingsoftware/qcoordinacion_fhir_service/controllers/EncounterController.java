package com.ingsoftware.qcoordinacion_fhir_service.controllers;
import com.ingsoftware.qcoordinacion_fhir_service.services.EncounterService;
import org.hl7.fhir.r5.model.Encounter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/encounters")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"}) // Configura los orígenes permitidos
public class EncounterController {
    @Autowired
    private EncounterService encounterService;
    @PostMapping
    public ResponseEntity<String> createEncounter(@RequestBody String encounter) {
        String encounterId = encounterService.createEncounter(encounter);
        return ResponseEntity.ok(encounterId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<String> setEncounterAsCompleted(@PathVariable String id){
        encounterService.setEncounterAsCompleted(id);
        return ResponseEntity.ok("Se cambio correctamente el estado");
    }
    @GetMapping("/{id}")
    public ResponseEntity<Encounter> getEncounter(@PathVariable String id) {
        Encounter cirugia = encounterService.getEncounterbyId(id);
        return ResponseEntity.ok(cirugia);
    }

    @GetMapping
    public ResponseEntity<String> getAllEncountersDataLinda(){
        return ResponseEntity.ok(encounterService.toJson().toString());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEncounter(@PathVariable String id) {
        encounterService.deleteEncounterById(id);
        return ResponseEntity.ok("DELETED");
    }
}
