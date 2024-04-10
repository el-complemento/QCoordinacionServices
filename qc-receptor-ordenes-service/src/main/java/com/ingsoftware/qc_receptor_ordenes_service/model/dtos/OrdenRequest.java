package com.ingsoftware.qc_receptor_ordenes_service.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenRequest {
    private Long ordenId;
    private Long pacienteId;
    private String codigoTerminologia;
}
