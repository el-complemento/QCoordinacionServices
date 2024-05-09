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
    //esto se mira y no se toca
    public Bundle getPractitionersByRole(String roleCode, String system) {
        system = "http://terminology.hl7.org/CodeSystem/".concat(system);
        return fhirClient
                .search()
                .forResource(PractitionerRole.class)
                .where(PractitionerRole.ROLE.exactly().systemAndCode(system, roleCode))
                .include(PractitionerRole.INCLUDE_PRACTITIONER)
                .returnBundle(Bundle.class)
                .execute();
    }}