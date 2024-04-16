package com.ingsoftware.qc_receptor_ordenes_service.repositories;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, String> {
    // Aquí puedes añadir métodos personalizados de consulta si lo necesitas
}
