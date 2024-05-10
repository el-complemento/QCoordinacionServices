package com.ingsoftware.qc_receptor_ordenes_service.model.dtos.frontend_Dto;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.ServiceRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateServiceRequestDto {
    private String resourceType;
    private String basedOn;
    private String status;
    private String priority;
    private String code;
    private String authoredOn;
    private String performerType;

    private String patient;
    private String practitioner;
    private String practitionerRole;

    public ServiceRequest toEntity() {
        ServiceRequest entity = new ServiceRequest();
        entity.setResourceType(this.resourceType);
        entity.setBasedOn(this.basedOn);
        entity.setStatus(this.status);
        entity.setPriority(this.priority);
        entity.setCode(this.code);
        entity.setAuthoredOn(this.authoredOn);
        entity.setPerformerType(this.performerType);
        return entity;
    }
}
