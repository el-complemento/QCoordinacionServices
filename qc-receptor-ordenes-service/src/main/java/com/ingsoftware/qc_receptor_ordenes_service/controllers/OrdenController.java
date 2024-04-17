package com.ingsoftware.qc_receptor_ordenes_service.controllers;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Orden;
import com.ingsoftware.qc_receptor_ordenes_service.services.OrdenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
public class OrdenController {
    @Autowired
    private OrdenService ordenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Orden> createOrden(@RequestBody Orden orden) {
        Orden newOrden = ordenService.saveOrden(orden);
        return ResponseEntity.ok(newOrden);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Orden> getOrdenById(@PathVariable String id) {
        return ordenService.getOrdenById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CrossOrigin(origins = "*")
    public List<Orden> getAllOrdenes() {
        return ordenService.getAllOrdenes();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteOrden(@PathVariable String id) {
        ordenService.deleteOrden(id);
        return ResponseEntity.ok().build();
    }
}
