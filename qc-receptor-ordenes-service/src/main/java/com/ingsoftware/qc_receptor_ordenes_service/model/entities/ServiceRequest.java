package com.ingsoftware.qc_receptor_ordenes_service.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServiceRequest {

    private String resourceType; // Service Request
    private String basedOn; // idDelPadre si es preoperatorio vacio si es importanto
    private String status; // completed active or on-hold
    private String priority; // routine urgent or asap
    private String code;
    private String authoredOn;
    private String performerType;
    private Patient patient;
}
