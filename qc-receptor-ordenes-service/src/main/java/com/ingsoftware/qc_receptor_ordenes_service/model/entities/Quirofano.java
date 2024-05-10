package com.ingsoftware.qc_receptor_ordenes_service.model.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quirofano {
    //private List<Disponibilidad> disponibilidades;
    //private List<PeriodoNoDisponible> periodosNoDisponibles;
    @Id
    private Integer numeroQuirofano;
}



