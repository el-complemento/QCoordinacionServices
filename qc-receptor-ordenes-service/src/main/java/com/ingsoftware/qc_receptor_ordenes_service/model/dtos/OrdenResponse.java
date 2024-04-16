package com.ingsoftware.qc_receptor_ordenes_service.model.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenResponse {
    private Long numeroOrden;
    private Long pacienteId;
    private String codigoTerminologia;
}
