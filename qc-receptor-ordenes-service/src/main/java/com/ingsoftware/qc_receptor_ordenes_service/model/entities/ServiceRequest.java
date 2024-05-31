package com.ingsoftware.qc_receptor_ordenes_service.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingsoftware.qc_receptor_ordenes_service.model.entities.util.*;
import com.ingsoftware.qc_receptor_ordenes_service.model.enums.StatusEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRequest {

    private String resourceType;
    private BasedOn basedOn;
    private Subject subject;
    private StatusEnum status;
    private String priority;
    private Code code;
    private OcurrenceTiming ocurrenceTiming;
    private Requester requester;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String identifier;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String authoredOn;

    private PerformerType performerType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Patient patient;

    public void setCoding(String system, String code, String text) {
        Code newCode = new Code();
        Concept concept = new Concept();
        Coding coding = new Coding();

        coding.setSystem(system);
        coding.setCode(code);

        List<Coding> codingList = new ArrayList<>();
        codingList.add(coding);

        concept.setCoding(codingList);
        concept.setText(text);

        newCode.setConcept(concept);

        this.setCode(newCode);
    }

    public void setPerformerType(String system, String code, String display) {
        PerformerType performerType = new PerformerType();
        PerformerTypeCoding performerCoding = new PerformerTypeCoding();

        performerCoding.setSystem(system);
        performerCoding.setCode(code);
        performerCoding.setDisplay(display);

        List<PerformerTypeCoding> performerCodingList = new ArrayList<>();
        performerCodingList.add(performerCoding);

        performerType.setCoding(performerCodingList);

        this.setPerformerType(performerType);
    }

    public void setBasedOnReference(String reference) {
        if (this.basedOn == null) {
            this.basedOn = new BasedOn();
        }
        this.basedOn.setReference(reference);
    }

    public void setRequester(String reference) {
        if (this.requester == null) {
            this.requester = new Requester();
        }
        this.requester.setReference(reference);
    }

    public void setOcurrenceTiming(String duration, String durationUnit) {
        // pusimos durationUnit xq sino dsp cuando se llama abajo te hace problemas
        OcurrenceTiming ocurrenceTiming = new OcurrenceTiming();
        Repeat repeat = new Repeat();
        repeat.setDuration(duration);
        ocurrenceTiming.setRepeat(repeat);

        this.setOcurrenceTiming(ocurrenceTiming);
    }
}