package com.ingsoftware.qc_receptor_ordenes_service.repositories;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Quirofano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuirofanoRepository extends JpaRepository<Quirofano,Long> {
}
