package com.ingsoftware.qc_receptor_ordenes_service.services;

import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.OrdenRequest;
import com.ingsoftware.qc_receptor_ordenes_service.model.dtos.OrdenResponse;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Orden;
import com.ingsoftware.qc_receptor_ordenes_service.repositories.OrdenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdenService {
    private final OrdenRepository ordenRepository;

    public void addOrden(OrdenRequest ordenRequest) {
        var orden = Orden.builder()
                .id(ordenRequest.getOrdenId())
                .pacienteId(ordenRequest.getPacienteId())
                .codigoTerminologia(ordenRequest.getCodigoTerminologia())
                .build();

        ordenRepository.save(orden);
        log.info("Orden added: {}", orden);
    }

    public List<OrdenResponse> getAllOrdenes(){
        var ordenes = ordenRepository.findAll();

        return ordenes.stream().map(this::mapToOrdenResponse).toList();
    }

    private OrdenResponse mapToOrdenResponse(Orden orden) {
        return OrdenResponse.builder()
                .ordenId(orden.getId())
                .pacienteId(orden.getPacienteId())
                .codigoTerminologia(orden.getCodigoTerminologia())
                .build();
    }
}
