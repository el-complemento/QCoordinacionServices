package com.ingsoftware.qcalgoritmo.Entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Quirofano {
    public enum Estado{Disponible,NoDisponible}
    private Integer numeroQuirofano;
    private Estado estado;
}
