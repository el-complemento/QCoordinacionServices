package com.ingsoftware.qc_receptor_ordenes_service.model.entities;

import jakarta.persistence.*;

import lombok.Data;  // Lombok annotation for getter, setter, toString, equals, and hashCode methods
import lombok.NoArgsConstructor;  // Lombok annotation to generate a no-arguments constructor
import lombok.AllArgsConstructor;  // Lombok annotation to generate a constructor with all arguments

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Cirugia {
    public enum Estado{Terminada, Programada, Recomendada}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCirugia;

    private Estado estado;

    private Date fecha; // hay q ver q esto este bien, dale

    private String codigoTerminologia;
}

