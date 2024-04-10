package com.ingsoftware.qc_receptor_ordenes_service.model.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pacienteId;
    private String codigoTerminologia;

    @Override
    public String toString() {
        return "Orden{" +
                "id=" + id +
                ", pacienteId=" + pacienteId +
                ", codigoTerminologia='" + codigoTerminologia + '\'' +
                '}';
    }
}
