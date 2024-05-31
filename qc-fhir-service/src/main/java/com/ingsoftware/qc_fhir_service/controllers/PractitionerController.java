package com.ingsoftware.qc_fhir_service.controllers;

import com.ingsoftware.qc_fhir_service.services.PractitionerService;
import org.hl7.fhir.r5.model.Practitioner;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/practicioners")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8081"}) // Configura los orígenes permitidos
public class PractitionerController {
    @Autowired
    private PractitionerService practitionerService;

    @PutMapping
    public ResponseEntity<String> createPracticioner(@RequestBody String practitioner) {
        String practitionerId = practitionerService.createPractitioner(practitioner);
        return ResponseEntity.ok(practitionerId);
    }
  
    @GetMapping
    public ResponseEntity<String> getPractitionersConCedula() {
        JSONArray practitioners = practitionerService.getAllPractitionersCedulas();
        return ResponseEntity.ok(practitioners.toString());
    }
  
    @GetMapping("/{id}")
    public ResponseEntity<Practitioner> getPractitioner(@PathVariable String id){
        Practitioner practitioner = practitionerService.getPractitioner(id);
        return ResponseEntity.ok(practitioner);
    }
  
    @GetMapping("/nombre/{id}")
    public ResponseEntity<String> getPractitionerName(@PathVariable String id){
        String nombre = "";
        Practitioner practitioner = practitionerService.getPractitioner(id);
        nombre=practitioner.getNameFirstRep().getGiven().get(0).toString();
        nombre+=" "+practitioner.getNameFirstRep().getFamily();
        return ResponseEntity.ok(nombre);
    }

}
