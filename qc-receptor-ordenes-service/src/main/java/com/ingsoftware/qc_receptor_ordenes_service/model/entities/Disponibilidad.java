package com.ingsoftware.qc_receptor_ordenes_service.model.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Disponibilidad {
    public enum DiaDeSemana {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
    }
    private List<DiaDeSemana> diasDeSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    // Getters y Setters
    public List<DiaDeSemana> getDiasDeSemana() {
        return diasDeSemana;
    }

    public void setDiasDeSemana(List<DiaDeSemana> diasDeSemana) {
        this.diasDeSemana = diasDeSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }
}

