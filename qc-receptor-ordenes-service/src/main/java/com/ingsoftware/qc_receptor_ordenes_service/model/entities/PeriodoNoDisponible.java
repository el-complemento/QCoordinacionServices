package com.ingsoftware.qc_receptor_ordenes_service.model.entities;

import java.time.LocalDate;

public class PeriodoNoDisponible {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Getters y Setters
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

}