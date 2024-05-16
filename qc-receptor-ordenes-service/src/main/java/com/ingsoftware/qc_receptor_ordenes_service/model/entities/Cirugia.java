package com.ingsoftware.qc_receptor_ordenes_service.model.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cirugia {
    public enum Estado{Terminada, Programada, Recomendada}
    @Id
    private Long idCirugia;
    private Estado estado;
    private Date fecha;
    private Integer numeroQuirofano;
    private String numeroOrdenAsociada;


    @Override
    public String toString() {
        return "Cirugia{" +
                "idCirugia=" + idCirugia +
                ", estado=" + estado +
                ", fecha='" + fecha.toString() + '\'' +
                '}';
    }

}



