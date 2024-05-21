package com.ingsoftware.qcoordinacion_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AppointmentService {
    @Autowired
    private IGenericClient fhirClient;
    private final FhirContext fhirContext = FhirContext.forR5();
    public String createAppointment(String appointment) {
        IParser parser = fhirContext.newJsonParser();
        Appointment nuevoAppointment = parser.parseResource(Appointment.class, appointment);
        MethodOutcome outcome = fhirClient.create().resource(nuevoAppointment).execute();
        return outcome.getId().getIdPart();
    }
    public String acceptAppointment(String idAppointment) {
        Appointment appointmentAceptada = fhirClient.read().resource(Appointment.class).withId(idAppointment).execute();
        Encounter nuevoEncounter = new Encounter();
        nuevoEncounter.setBasedOn(appointmentAceptada.getBasedOn());
        nuevoEncounter.setStatus(Enumerations.EncounterStatus.PLANNED);
        nuevoEncounter.setPriority(appointmentAceptada.getPriority());
        nuevoEncounter.setSubject(appointmentAceptada.getSubject());
        nuevoEncounter.setPlannedStartDate(appointmentAceptada.getStart());
        nuevoEncounter.setPlannedEndDate(appointmentAceptada.getEnd());
        for (Appointment.AppointmentParticipantComponent participantes : appointmentAceptada.getParticipant()) {
            if (Objects.equals(participantes.getActor().getReference().split("/")[0], "Practitioner")) {
                Encounter.EncounterParticipantComponent nuevoDoctor = new Encounter.EncounterParticipantComponent();
                nuevoDoctor.setActor(participantes.getActor());
                List<Encounter.EncounterParticipantComponent> listaNueva = nuevoEncounter.getParticipant();
                listaNueva.add(nuevoDoctor);
                nuevoEncounter.setParticipant(listaNueva);
            }
            if (Objects.equals(participantes.getActor().getReference().split("/")[0], "Location")) {
                Encounter.EncounterLocationComponent quirofano = new Encounter.EncounterLocationComponent();
                quirofano.setLocation(participantes.getActor());
                List<Encounter.EncounterLocationComponent> quirofanoEnLista = new ArrayList<>();
                quirofanoEnLista.add(quirofano);
                nuevoEncounter.setLocation(quirofanoEnLista);
            }
        }
        MethodOutcome outcome = fhirClient.create().resource(nuevoEncounter).execute();
        return outcome.getId().getIdPart();
    }
    public List<String> mostrarAppointmentsPlaneados(){
        IParser parser = fhirContext.newJsonParser();
        List<String> appointmentsJson = new ArrayList<>();
        Bundle appointments = fhirClient.search().forResource(Appointment.class).where(Appointment.STATUS.exactly().code("proposed")).returnBundle(Bundle.class).execute();
        for (Bundle.BundleEntryComponent entry : appointments.getEntry()) {
            Appointment appointment = (Appointment) entry.getResource();
            String startDate = appointment.getStart().toString();
            String endDate = appointment.getEnd().toString();
            String prioridad = appointment.getPriority().getCoding().get(0).getCode();
            List<String> doctores =new ArrayList<>();
            String paciente = appointment.getSubject().getReference().split("/")[1];
            String quirofano ="";
            String idOrden = appointment.getBasedOn().get(0).getReference().split("/")[1];
            for (Appointment.AppointmentParticipantComponent participantComponent : appointment.getParticipant()) {
                if (Objects.equals(participantComponent.getActor().getReference().split("/")[0], "Practitioner")) {
                    Practitioner practitioner = fhirClient.read().resource(Practitioner.class).withId(participantComponent.getActor().getReference().split("/")[1]).execute();
                    String nombreCompleto="";
                    nombreCompleto=nombreCompleto.concat(String.valueOf(practitioner.getName().get(0).getGiven().get(0)));
                    nombreCompleto=nombreCompleto.concat(" ");
                    nombreCompleto=nombreCompleto.concat((practitioner.getName().get(0).getFamily()));
                    doctores.add(nombreCompleto);
                }
                else {
                     quirofano=participantComponent.getActor().getReference().split("/")[1];
                }
            }
            ServiceRequest orden=fhirClient.read().resource(ServiceRequest.class).withId(appointment.getBasedOn().get(0).getReference().split("/")[1]).execute();
            String title= orden.getCode().getConcept().getText();
            String json = String.format("{title: '%s', start: '%s', end: '%s', priority: '%s', doctores: %s, paciente: '%s', idOrden: '%s', quirofano: '%s'}",
                    title,
                    startDate,
                    endDate,
                    prioridad,
                    doctores,
                    paciente,
                    idOrden,
                    quirofano); // Se añade el campo quirofano aquí
            appointmentsJson.add(json);
        }
        return appointmentsJson;
    }
    public void deleteAppointmentById(String appointmentId) {
        try {
            // Crear el IdType con el tipo de recurso y el ID
            IdType resourceId = new IdType("Appointment", appointmentId);

            // Eliminar el recurso Encounter especificado
            fhirClient.delete().resourceById(resourceId).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
