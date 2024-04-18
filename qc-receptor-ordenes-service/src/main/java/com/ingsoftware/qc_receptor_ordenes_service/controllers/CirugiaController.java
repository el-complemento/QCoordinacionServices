package com.ingsoftware.qc_receptor_ordenes_service.controllers;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Cirugia;
import com.ingsoftware.qc_receptor_ordenes_service.services.CirugiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cirugias")
public class CirugiaController {
    @Autowired
    private CirugiaService cirugiaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Cirugia> createCirugia(@RequestBody Cirugia cirugia) {
        Cirugia newCirugia = cirugiaService.saveCirugia(cirugia);
        return ResponseEntity.ok(newCirugia);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Cirugia> getCirugiaById(@PathVariable Long id) {
        return cirugiaService.getCirugiaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    public List<Cirugia> getAllCirugias() {
        return cirugiaService.getAllCirugias();}


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteCirugia(@PathVariable Long id) {
        cirugiaService.deleteCirugia(id);
        return ResponseEntity.ok().build();
    }
    }
