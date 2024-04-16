package com.ingsoftware.qc_receptor_ordenes_service.services;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Orden;
import com.ingsoftware.qc_receptor_ordenes_service.repositories.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrdenService {
    @Autowired
    private OrdenRepository ordenRepository;

    public Orden saveOrden(Orden orden) {
        return ordenRepository.save(orden);
    }

    public Optional<Orden> getOrdenById(String id) {
        return ordenRepository.findById(id);
    }

    public List<Orden> getAllOrdenes() {
        return ordenRepository.findAll();
    }

    public void deleteOrden(String id) {
        ordenRepository.deleteById(id);
    }

    // Aquí puedes añadir más métodos según la lógica de tu negocio
}
