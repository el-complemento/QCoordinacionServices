package com.ingsoftware.qc_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r5.model.HumanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hl7.fhir.r5.model.Practitioner;

import java.util.List;

@Service
public class PractitionerService {
    @Autowired
    private IGenericClient fhirClient;
    private final FhirContext fhirContext = FhirContext.forR5();
    public String createPractitioner(String practitioner) {
        IParser parser =  fhirContext.newJsonParser();
        Practitioner nuevoPractitioner = parser.parseResource(Practitioner.class, practitioner);
        MethodOutcome outcome = fhirClient.update().resource(nuevoPractitioner).execute();
        return outcome.getId().getIdPart();
    }
    public Practitioner getPractitioner(String idPractitioner) {
        return fhirClient.read().resource(Practitioner.class).withId(idPractitioner).execute();

    }
    public  HumanName getPractitionerNombreById(String id) {
        Practitioner practitioner = fhirClient.read().resource(Practitioner.class).withId(id).execute();
        List<HumanName> names = practitioner.getName();
        return practitioner.getNameFirstRep();
    }
}
