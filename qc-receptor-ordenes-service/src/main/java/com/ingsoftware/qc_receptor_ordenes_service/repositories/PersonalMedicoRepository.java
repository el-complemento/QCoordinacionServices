package com.ingsoftware.qc_receptor_ordenes_service.repositories;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.PersonalMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalMedicoRepository extends JpaRepository<PersonalMedico,Long>{
}
