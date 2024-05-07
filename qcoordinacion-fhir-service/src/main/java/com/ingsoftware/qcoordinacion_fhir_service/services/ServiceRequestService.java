package com.ingsoftware.qcoordinacion_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.ServiceRequest;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ServiceRequestService {
    @Autowired
    private IGenericClient fhirClient;
    private final FhirContext fhirContext = FhirContext.forR5();

    public String createServiceRequest(String serviceRequest) {
        IParser parser =  fhirContext.newJsonParser();
        ServiceRequest nuevaServiceRequest = parser.parseResource(ServiceRequest.class, serviceRequest);
        MethodOutcome outcome = fhirClient.create().resource(nuevaServiceRequest).execute();
        return outcome.getId().getIdPart();
    }
    public ServiceRequest getServiceRequestById(String id) {
        // Obtiene el paciente por su ID desde el servidor FHIR
        return fhirClient.read().resource(ServiceRequest.class).withId(id).execute();
    }
    public Bundle getServiceRequestsFromDate(String date) {
        return fhirClient.search().forResource(ServiceRequest.class)
                .where(new DateClientParam("authored").afterOrEquals().day(date))
                .returnBundle(Bundle.class)
                .execute();
    }
}

