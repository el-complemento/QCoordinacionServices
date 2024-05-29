package com.ingsoftware.qc_receptor_ordenes_service.model.dtos;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.ServiceRequest;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateServiceRequestDto {
    // esto calculo q es para la ventana de "Crear Orden"
    private String basedOn; // procedimiento??
    private String requester; // el doctor q lo pidio
    private String patientId;
    private String priority;
    private String code; // no tiene code en la parte de crear orden
    private String authoredOn; //fecha q se creo
    private String performerTypeCode; // roles
    private String ocurrenceTiming;
    private List<CreateServiceRequestDto> preOperative;

    public ServiceRequest toEntity() {
        ServiceRequest entity = new ServiceRequest();
        entity.setBasedOnReference(basedOn);
        entity.setPriority(this.priority);
        entity.setAuthoredOn(this.authoredOn);
        return entity;
    }
}