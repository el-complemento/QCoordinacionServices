package com.ingsoftware.qc_receptor_ordenes_service.repositories;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Cirugia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CirugiaRepository extends JpaRepository<Cirugia,Long> {
}
