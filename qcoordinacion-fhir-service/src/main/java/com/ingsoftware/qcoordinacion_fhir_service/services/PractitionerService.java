package com.ingsoftware.qcoordinacion_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hl7.fhir.r5.model.Practitioner;

@Service
public class PractitionerService {
    @Autowired
    private IGenericClient fhirClient;
    private final FhirContext fhirContext = FhirContext.forR5();
    public String createPractitioner(String practitioner) {
        IParser parser =  fhirContext.newJsonParser();
        Practitioner nuevoPractitioner = parser.parseResource(Practitioner.class, practitioner);
        MethodOutcome outcome = fhirClient.create().resource(nuevoPractitioner).execute();
        return outcome.getId().getIdPart();
    }
    public Practitioner getPractitioner(String idPractitioner) {
        return fhirClient.read().resource(Practitioner.class).withId(idPractitioner).execute();

    }
}
