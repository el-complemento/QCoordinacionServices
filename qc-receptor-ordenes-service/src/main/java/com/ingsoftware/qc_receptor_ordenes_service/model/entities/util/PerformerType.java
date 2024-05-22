package com.ingsoftware.qc_receptor_ordenes_service.model.entities.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PerformerType {
    private List<PerformerTypeCoding> coding;
}
