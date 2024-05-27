package com.ingsoftware.qc_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.ServiceRequest;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public ServiceRequest markServiceRequestAsCompleted(String id) {
        ServiceRequest serviceRequest = getServiceRequestById(id);
        if(!serviceRequest.hasBasedOn()){
        serviceRequest.setStatus(Enumerations.RequestStatus.COMPLETED);
        MethodOutcome outcome = fhirClient.update().resource(serviceRequest).execute();
        return (ServiceRequest) outcome.getResource();
        }
        else{
            Boolean todasSubOrdenesCompletadas= true;
            serviceRequest.setStatus(Enumerations.RequestStatus.COMPLETED);
            MethodOutcome outcome = fhirClient.update().resource(serviceRequest).execute();
            String idOrdenPadre = serviceRequest.getBasedOn().get(0).getId();
            Bundle subOrdenes = fhirClient.search().forResource(ServiceRequest.class)
                    .where(ServiceRequest.BASED_ON.hasId(idOrdenPadre))
                    .returnBundle(Bundle.class)
                    .execute();
            for (Bundle.BundleEntryComponent entry : subOrdenes.getEntry()) {
                if (entry.getResource() instanceof ServiceRequest) {
                    ServiceRequest orden = (ServiceRequest) entry.getResource();
                    if(!orden.getStatus().equals(Enumerations.RequestStatus.COMPLETED)){
                        todasSubOrdenesCompletadas=false;
                    }
                }
            }
            if(todasSubOrdenesCompletadas){
                getServiceRequestById(idOrdenPadre).setStatus(Enumerations.RequestStatus.ACTIVE);
                MethodOutcome outcome2 = fhirClient.update().resource(serviceRequest).execute();
            }
            return (ServiceRequest) outcome.getResource();
        }

    }
}

