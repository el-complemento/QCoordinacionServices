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
    public JSONArray getDisponiblidadesRoles() {
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
                    roleInfo.put("rol", role.getCode().get(0).getCoding().get(0).getCode());
                }

                // Agregar información de disponibilidad
                JSONArray availabilities = new JSONArray();
                if (role.getAvailability() != null && !role.getAvailability().isEmpty()) {
                    for (Availability time : role.getAvailability()) {
                        JSONObject availability = new JSONObject();
                        availability.put("daysOfWeek", time.getAvailableTime().get(0).getDaysOfWeek().toString());
                        if (time.getAvailableTime().get(0).getAllDay()) {
                            availability.put("allDay", true);
                        } else {
                            availability.put("availableStartTime", time.getAvailableTime().get(0).getAvailableStartTime());
                            availability.put("availableEndTime", time.getAvailableTime().get(0).getAvailableEndTime());
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
    }}