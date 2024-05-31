package com.ingsoftware.qc_receptor_ordenes_service.model.entities.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// vamos a hcer una clase repeat para poner adentro duration y durunit
public class Repeat {
    private String duration;
//    private String durationUnit;  lo scamos xq siempre usamos horas
}
