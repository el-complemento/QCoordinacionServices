package com.ingsoftware.qcoordinacion_fhir_service.controllers;

import com.ingsoftware.qcoordinacion_fhir_service.services.AppointmentService;
import com.ingsoftware.qcoordinacion_fhir_service.services.EncounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/appointments")
@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<String> createAppointment(@RequestBody String appointment) {
        String appointmentId = appointmentService.createAppointment(appointment);
        return ResponseEntity.ok(appointmentId);
    }
    @GetMapping("/aceptar/{id}")
    public ResponseEntity<String> acceptAppointment(@PathVariable String id){
        return ResponseEntity.ok(appointmentService.acceptAppointment(id));
    }
    @GetMapping("/recomendadas")
    public ResponseEntity<List<String>> mostrarAppointmentsRecomendadas(){
        return ResponseEntity.ok(appointmentService.mostrarAppointmentsPlaneados());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEncounter(@PathVariable String id) {
        appointmentService.deleteAppointmentById(id);
        return ResponseEntity.ok("DELETED");
    }
}
