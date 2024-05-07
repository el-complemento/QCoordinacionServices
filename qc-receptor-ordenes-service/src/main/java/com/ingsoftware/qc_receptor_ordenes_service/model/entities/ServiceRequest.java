package com.ingsoftware.qc_receptor_ordenes_service.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingsoftware.qc_receptor_ordenes_service.model.enums.StatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String identifier;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String authoredOn;

    private PerformerType performerType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Patient patient;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Subject {
        private String reference;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Code {
        private Concept concept;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Concept {
            private List<Coding> coding;
            private String text;

            @Getter
            @Setter
            @NoArgsConstructor
            public static class Coding {
                private String system;
                private String code;
            }
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class BasedOn {
        private String display;

        public void setReference(String display) {
            this.display = display;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PerformerType {
        private List<Coding> coding;

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Coding {
            private String system;
            private String code;
            private String display;
        }
    }

    public void setBasedOnReference(String reference) {
        if (this.basedOn == null) {
            this.basedOn = new BasedOn();
        }
        this.basedOn.setReference(reference);
    }
    public void setCoding(String system, String code, String text) {
        Code.Concept.Coding coding = new Code.Concept.Coding();
        coding.setSystem(system);
        coding.setCode(code);

        Code.Concept concept = new Code.Concept();
        concept.setCoding(List.of(coding));
        concept.setText(text);

        this.code = new Code();
        this.code.setConcept(concept);
    }

    public void setPerformerType(String system, String code, String display) {
        PerformerType.Coding coding = new PerformerType.Coding();
        coding.setSystem(system);
        coding.setCode(code);
        coding.setDisplay(display);

        this.performerType = new PerformerType();
        this.performerType.setCoding(List.of(coding));
    }
}