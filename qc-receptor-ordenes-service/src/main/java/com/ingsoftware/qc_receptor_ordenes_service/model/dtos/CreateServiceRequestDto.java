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
    private String basedOn;
    private String priority;
    private String code;
    private String authoredOn;
    private String performerTypeCode;

    private String patientId;

    private List<CreateServiceRequestDto> preOperative;

    public ServiceRequest toEntity() {
        ServiceRequest entity = new ServiceRequest();
        entity.setBasedOnReference(this.basedOn);
        entity.setPriority(this.priority);
        entity.setAuthoredOn(this.authoredOn);
        return entity;
    }
}