package com.ingsoftware.qc_receptor_ordenes_service.model.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SiCoDiDto{
    private String system = "http://snomed.ct";
    private String code;
    private String display;
}

