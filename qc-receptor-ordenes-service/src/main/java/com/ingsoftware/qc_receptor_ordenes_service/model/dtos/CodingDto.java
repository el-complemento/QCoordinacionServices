package com.ingsoftware.qc_receptor_ordenes_service.model.dtos;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CodingDto {
    List<SiCoDiDto> siCoDiDtos;
}
