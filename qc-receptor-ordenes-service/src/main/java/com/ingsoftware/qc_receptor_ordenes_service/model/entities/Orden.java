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
    private String numeroOrden;
    private Long pacienteId;
    private String codigoTerminologia;

    @Override
    public String toString() {
        return "Orden{" +
                "numeroOrden=" + numeroOrden +
                ", pacienteId=" + pacienteId +
                ", codigoTerminologia='" + codigoTerminologia + '\'' +
                '}';
    }
}

