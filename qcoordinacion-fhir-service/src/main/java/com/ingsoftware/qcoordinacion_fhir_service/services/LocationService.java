package com.ingsoftware.qcoordinacion_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    @Autowired
    private IGenericClient fhirClient;
    private final FhirContext fhirContext = FhirContext.forR5();

    public String createQuirofano(String quirofano) {
        IParser parser =  fhirContext.newJsonParser();
        Location nuevoQuirofano = parser.parseResource(Location.class,quirofano);
        MethodOutcome outcome = fhirClient.update().resource(nuevoQuirofano).execute();
        return outcome.getId().getIdPart();
    }
    public String getDisponibilidadQuirofano(String numeroQuirofano) {
        IParser parser = fhirContext.newJsonParser();
        Location quirofano = fhirClient.read().resource(Location.class).withId(numeroQuirofano).execute();
        Availability disponibilidadQuirofano = quirofano.getHoursOfOperation().get(0);
        return parser.encodeToString(disponibilidadQuirofano);
    }
}
