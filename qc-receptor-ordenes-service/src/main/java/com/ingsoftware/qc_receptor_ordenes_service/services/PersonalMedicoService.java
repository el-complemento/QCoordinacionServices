package com.ingsoftware.qc_receptor_ordenes_service.services;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Orden;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.PersonalMedico;
import com.ingsoftware.qc_receptor_ordenes_service.repositories.OrdenRepository;
import com.ingsoftware.qc_receptor_ordenes_service.repositories.PersonalMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalMedicoService {
    @Autowired
    private PersonalMedicoRepository personalMedicoRepository;
    public PersonalMedico savePersonalMedico(PersonalMedico personalMedico) {
        return personalMedicoRepository.save(personalMedico);
    }

    public Optional<PersonalMedico> getPersonalMedicoByDNI(Long dni) {
        return personalMedicoRepository.findById(dni);
    }

    public List<PersonalMedico> getAllPersonalMedico() {
        return personalMedicoRepository.findAll();
    }

    public void deletePersonalMedico(Long dni) {
        personalMedicoRepository.deleteById(dni);
    }
}
