package com.ingsoftware.qc_receptor_ordenes_service.model.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Patient {

    private String resourceType;
    private String id;
    private String gender;
    private Date birthDate;
    private String nameUse;
    private String nameFamily;
    private List<String> nameGiven;

    @JsonProperty("name")
    private void unpackName(List<Name> names) {
        if (names != null && !names.isEmpty()) {
            Name name = names.get(0);
            this.nameUse = name.getUse();
            this.nameFamily = name.getFamily();
            this.nameGiven = name.getGiven();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Name {
        private String use;
        private String family;
        private List<String> given;
    }
}