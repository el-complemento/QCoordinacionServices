package com.ingsoftware.qc_receptor_ordenes_service.services;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Cirugia;
import com.ingsoftware.qc_receptor_ordenes_service.repositories.CirugiaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CirugiaService {
    @Autowired
    private CirugiaRepository cirugiaRepository;

    public Cirugia saveCirugia(Cirugia cirugia) {
        return cirugiaRepository.save(cirugia);
    }

    public Optional<Cirugia> getCirugiaById(Long id) {
        return cirugiaRepository.findById(id);
    }

    public List<Cirugia> getAllCirugias() {
        return cirugiaRepository.findAll();
    }

    public void deleteCirugia(Long id) {
        cirugiaRepository.deleteById(id);
    }

    // Aquí puedes añadir más métodos según la lógica de tu negocio
}
