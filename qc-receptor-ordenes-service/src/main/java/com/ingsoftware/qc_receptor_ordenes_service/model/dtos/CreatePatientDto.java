package com.ingsoftware.qc_receptor_ordenes_service.model.dtos;

import com.ingsoftware.qc_receptor_ordenes_service.model.entities.Patient;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreatePatientDto {
    private String Id;
    private String NameUse;
    private List<String> NameGiven;
    private String NameFamily;
    private String Gender;
    private Date BirthDate;


    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setId(this.Id);
        patient.setNameUse(this.NameUse);
        patient.setNameGiven(this.NameGiven);
        patient.setNameFamily(this.NameFamily);
        patient.setGender(this.Gender);
        patient.setBirthDate(this.BirthDate);
        return patient;
    }

    public static CreatePatientDto fromEntity(Patient patient) {
        return CreatePatientDto.builder()
                .Id(patient.getId())
                .NameUse(patient.getNameUse())
                .NameGiven(patient.getNameGiven())
                .NameFamily(patient.getNameFamily())
                .Gender(patient.getGender())
                .BirthDate(patient.getBirthDate())
                .build();
    }
}