package com.ingsoftware.qcalgoritmo.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quirofano {
    public enum Estado{Disponible,NoDisponible}
    @Id
    private Integer numeroQuirofano;
    private Estado estado;
}
