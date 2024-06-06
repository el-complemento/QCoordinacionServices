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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PractitionerRoleService {
    @Autowired
    private IGenericClient fhirClient;
    private final FhirContext fhirContext = FhirContext.forR5();

    public String createPractitionerRole(String practitionerRole) {
        IParser parser = fhirContext.newJsonParser();
        PractitionerRole nuevoPractitioner = parser.parseResource(PractitionerRole.class, practitionerRole);
        MethodOutcome outcome = fhirClient.create().resource(nuevoPractitioner).execute();
        return outcome.getId().getIdPart();
    }
    public PractitionerRole getPractitionerRole(String idPractitioner) {
        Bundle results=fhirClient
                .search()
                .forResource(PractitionerRole.class)
                .where(PractitionerRole.PRACTITIONER.hasId(idPractitioner))
                .returnBundle(Bundle.class)
                .execute();
        return (PractitionerRole) results.getEntry().get(0).getResource();
    }
    public String getPractitionerDisponibilidad(String idPractitioner) {
        IParser parser = fhirContext.newJsonParser();
        Bundle results=fhirClient
                .search()
                .forResource(PractitionerRole.class)
                .where(PractitionerRole.PRACTITIONER.hasId(idPractitioner))
                .returnBundle(Bundle.class)
                .execute();
        PractitionerRole role = (PractitionerRole) results.getEntry().get(0).getResource();
        Availability avilabilityDelMedico = role.getAvailability().get(0);
        return parser.encodeToString(avilabilityDelMedico);
    }
    public JSONArray getDisponibilidadesRoles() {
        // Buscar todos los PractitionerRoles
        Bundle results = fhirClient
                .search()
                .forResource(PractitionerRole.class)
                .returnBundle(Bundle.class)
                .execute();

        JSONArray disponibilidades = new JSONArray();

        // Iterar sobre cada PractitionerRole en el Bundle
        for (Bundle.BundleEntryComponent entry : results.getEntry()) {
            if (entry.getResource() instanceof PractitionerRole) {
                PractitionerRole role = (PractitionerRole) entry.getResource();

                JSONObject roleInfo = new JSONObject();
                // Extraer y añadir el ID del Practitioner desde la referencia
                Reference practitionerRef = role.getPractitioner();
                if (practitionerRef != null && practitionerRef.getReferenceElement() != null) {
                    roleInfo.put("idPractitioner", practitionerRef.getReferenceElement().getIdPart());
                }
                // Agregar la información del rol
                if (role.getCode() != null && !role.getCode().isEmpty()) {
                    roleInfo.put("rol", role.getCode().get(0).getCoding().get(0).getDisplay());
                }

                // Agregar información de disponibilidad
                JSONArray availabilities = new JSONArray();
                if (role.getAvailability() != null && !role.getAvailability().isEmpty()) {
                    for (Availability time : role.getAvailability()) {
                        JSONObject availability = new JSONObject();
                        System.out.println(role.getPractitioner());
                        availability.put("daysOfWeek", time.getAvailableTime().get(0).getDaysOfWeek().toString());

                        // Check for all-day availability
                        if (time.getAvailableTime().get(0).getAllDay()) {
                            availability.put("allDay", true);
                        } else {
                            availability.put("availableStartTime", time.getAvailableTime().get(0).getAvailableStartTime());
                            availability.put("availableEndTime", time.getAvailableTime().get(0).getAvailableEndTime());
                        }

                        // Add "notAvailableTime" if present
                        if (time.getNotAvailableTime() != null && !time.getNotAvailableTime().isEmpty()) {
                            JSONArray notAvailable = new JSONArray();
                            for (Availability.AvailabilityNotAvailableTimeComponent timing : time.getNotAvailableTime()) {
                                JSONObject unavailableTime = new JSONObject();
                                unavailableTime.put("during", new JSONObject().put("start", timing.getDuring().getStart().toString())
                                        .put("end", timing.getDuring().getEnd().toString()));
                                notAvailable.put(unavailableTime);
                            }
                            availability.put("notAvailableTime", notAvailable);
                        }

                        availabilities.put(availability);
                    }
                }
                roleInfo.put("disponibilidad", availabilities);

                disponibilidades.put(roleInfo);
            }
        }
        return disponibilidades;
    }


    public Bundle getPractitionersByRole(String roleCode) {
        return fhirClient
                .search()
                .forResource(PractitionerRole.class)
                .where(PractitionerRole.ROLE.exactly().code(roleCode))
                .include(PractitionerRole.INCLUDE_PRACTITIONER)
                .returnBundle(Bundle.class)
                .execute();
    }
    public void updatePractitionerRoleAvailability(String practitionerRoleId, LocalDateTime startDate, LocalDateTime endDate) {
        // Buscar el PractitionerRole actual
        PractitionerRole practitionerRole = findPractitionerRoleByPractitionerId(practitionerRoleId);

        if (practitionerRole == null) {
            throw new IllegalArgumentException("PractitionerRole not found");
        }
        // Crear y configurar el nuevo período de no disponibilidad
        Availability.AvailabilityNotAvailableTimeComponent notAvailableTime = new Availability.AvailabilityNotAvailableTimeComponent();
        Date start = convertToDate(startDate);
        Date end = convertToDate(endDate);
        Period period = new Period();
        period.setStart(start);
        period.setEnd(end);
        notAvailableTime.setDuring(period);
        // Agregar la información de no disponibilidad al PractitionerRole
        List<Availability> disponiblidadVieja = new ArrayList<>();
        disponiblidadVieja.add(practitionerRole.getAvailability().get(0));
        List<Availability>disponibilidadNueva = new ArrayList<>();
        disponibilidadNueva.add(disponiblidadVieja.get(0).addNotAvailableTime(notAvailableTime));
        practitionerRole.setAvailability(disponibilidadNueva);
        fhirClient.update().resource(practitionerRole).execute();
    }
    private PractitionerRole findPractitionerRoleByPractitionerId(String practitionerId) {
        // Buscar PractitionerRole que corresponda al ID del Practitioner
        Bundle results = fhirClient.search()
                .forResource(PractitionerRole.class)
                .where(PractitionerRole.PRACTITIONER.hasId(practitionerId))
                .returnBundle(Bundle.class)
                .execute();

        if (!results.getEntry().isEmpty()) {
            // Devuelve el primer PractitionerRole encontrado (puedes ajustar esto según tus necesidades)
            return (PractitionerRole) results.getEntry().get(0).getResource();
        }
        return null;
    }
    public void deleteRoleById(String roleId) {
        try {
            // Crear el IdType con el tipo de recurso y el ID
            IdType resourceId = new IdType("PractitionerRole", roleId);

            // Eliminar el recurso Encounter especificado
            fhirClient.delete().resourceById(resourceId).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Date convertToDate(LocalDateTime localDateTime) {
        // Considerando que la fecha y hora son en zona UTC
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}