package com.ingsoftware.qc_receptor_ordenes_service.model.entities;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PersonalMedico {
    public enum Rol{Enfermero,Cirujano,Anestecista}
    @Id
    private Long cedula;
    private String nombre;
    private String apellido;
    private Rol rol;

    @Override
    public String toString() {
        return "PersonalMedico{" +
                "Cedula=" + cedula +
                ", Rol=" + rol +
                ", Apellido=" + apellido +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}