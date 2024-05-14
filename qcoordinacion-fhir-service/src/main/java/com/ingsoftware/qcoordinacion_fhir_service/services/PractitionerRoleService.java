package com.ingsoftware.qcoordinacion_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r5.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        PractitionerRole role = (PractitionerRole) results.getEntry().get(0).getResource();
        return role;
    }

    //esto se mira y no se toca
    public Bundle getPractitionersByRole(String roleCode) {
        return fhirClient
                .search()
                .forResource(PractitionerRole.class)
                .where(PractitionerRole.ROLE.exactly().code(roleCode))
                .include(PractitionerRole.INCLUDE_PRACTITIONER)
                .returnBundle(Bundle.class)
                .execute();
    }}