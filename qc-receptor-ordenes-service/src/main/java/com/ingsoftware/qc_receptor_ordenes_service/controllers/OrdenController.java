package com.ingsoftware.qc_receptor_ordenes_service.controllers;

import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.OrdenRequest;
import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.OrdenResponse;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Orden;
import com.ingsoftware.qc_receptor_ordenes_service.services.OrdenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orden")
@RequiredArgsConstructor
public class OrdenController {
    private final OrdenService ordenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addOrden(@RequestBody OrdenRequest ordenRequest) {
        this.ordenService.addOrden(ordenRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrdenResponse> getOrdens() {
        return this.ordenService.getAllOrdenes();
    }
}
