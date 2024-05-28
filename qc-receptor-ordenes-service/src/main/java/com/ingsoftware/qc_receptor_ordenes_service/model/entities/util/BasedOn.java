package com.ingsoftware.qc_receptor_ordenes_service.model.entities.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasedOn {
    private String display;

    public void setReference(String display) {
        this.display = display;
    }
}
