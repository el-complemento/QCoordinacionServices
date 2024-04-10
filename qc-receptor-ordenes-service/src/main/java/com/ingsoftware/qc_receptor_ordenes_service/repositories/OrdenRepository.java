package com.ingsoftware.qc_receptor_ordenes_service.repositories;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}
