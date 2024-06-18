package com.ingsoftware.qc_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r5.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
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
    public IParser parser = fhirContext.newJsonParser();

    public String createAppointment(String appointment) {
        Appointment nuevoAppointment = (Appointment) parser.parseResource(appointment);
        MethodOutcome outcome = fhirClient.create().resource(nuevoAppointment).execute();
        return outcome.getId().getIdPart();
    }
    public String acceptAppointment(String idAppointment) {
        Appointment appointmentAceptada = fhirClient.read().resource(Appointment.class).withId(idAppointment).execute();

        // Cambiando estado de la appointment aceptada
        appointmentAceptada.setStatus(Appointment.AppointmentStatus.BOOKED);

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

        fhirClient.update().resource(appointmentAceptada).execute();

        return outcome.getId().getIdPart();
    }
    public JSONArray mostrarAppointmentsPlaneados() {
        IParser parser = fhirContext.newJsonParser();
        JSONArray appointmentsJson = new JSONArray();
        Bundle appointments = fhirClient.search().forResource(Appointment.class)
                .where(Appointment.STATUS.exactly().code("proposed"))
                .returnBundle(Bundle.class).execute();

        for (Bundle.BundleEntryComponent entry : appointments.getEntry()) {
            Appointment appointment = (Appointment) entry.getResource();
            JSONObject appointmentObj = new JSONObject();
            String startDate = appointment.getStart().toString();
            String endDate = appointment.getEnd().toString();
            String prioridad = appointment.getPriority().getCoding().get(0).getCode();
            JSONArray doctores = new JSONArray();
            String paciente = appointment.getSubject().getReference().split("/")[1];
            String quirofano = "";
            String idOrden = appointment.getBasedOn().get(0).getReference().split("/")[1];

            for (Appointment.AppointmentParticipantComponent participantComponent : appointment.getParticipant()) {
                if (Objects.equals(participantComponent.getActor().getReference().split("/")[0], "Practitioner")) {
                    Practitioner practitioner = fhirClient.read().resource(Practitioner.class)
                            .withId(participantComponent.getActor().getReference().split("/")[1])
                            .execute();
                    String nombreCompleto = practitioner.getName().get(0).getGiven().get(0) + " " + practitioner.getName().get(0).getFamily();
                    doctores.put(nombreCompleto);
                } else {
                    quirofano = participantComponent.getActor().getReference().split("/")[1];
                }
            }

            ServiceRequest orden = fhirClient.read().resource(ServiceRequest.class)
                    .withId(appointment.getBasedOn().get(0).getReference().split("/")[1])
                    .execute();
            String title = orden.getCode().getConcept().getText();

            appointmentObj.put("title", title);
            appointmentObj.put("start", startDate);
            appointmentObj.put("end", endDate);
            appointmentObj.put("priority", prioridad);
            appointmentObj.put("doctores", doctores);
            appointmentObj.put("paciente", paciente);
            appointmentObj.put("idOrden", idOrden);
            appointmentObj.put("quirofano", quirofano);
            appointmentObj.put("idAppontment", appointment.getIdPart());

            appointmentsJson.put(appointmentObj);
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
