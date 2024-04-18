package com.ingsoftware.qc_receptor_ordenes_service.controllers;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Orden;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.PersonalMedico;
import com.ingsoftware.qc_receptor_ordenes_service.services.OrdenService;
import com.ingsoftware.qc_receptor_ordenes_service.services.PersonalMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/personalmedico")
public class PersonalMedicoController {
    @Autowired
    private PersonalMedicoService personalMedicoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PersonalMedico> createPersonalMedico(@RequestBody PersonalMedico personalMedico) {
        PersonalMedico newPersonalMedico = personalMedicoService.savePersonalMedico(personalMedico);
        return ResponseEntity.ok(newPersonalMedico);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    public List<PersonalMedico> getAllPersonalMedico() {
        return personalMedicoService.getAllPersonalMedico();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deletePersonalMedico(@PathVariable Long dni) {
        personalMedicoService.deletePersonalMedico(dni);
        return ResponseEntity.ok().build();
    }
}
