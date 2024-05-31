package com.ingsoftware.qc_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.DateClientParam;
import org.hl7.fhir.r5.model.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
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

    public JSONArray fetchOnHoldServiceRequestsWithBasedOnNull() {
        List<JSONObject> ordenesJson = new ArrayList<>();
        // Recupera todos los ServiceRequest
        Bundle results = fhirClient
                .search()
                .forResource(ServiceRequest.class)
                .returnBundle(Bundle.class)
                .count(1000000) // OJO CON ESTO ES EL LIMITE DE LA CANTIDAD DE COSAS QUE PUEDE VENIR EN UN BUNDLE ESTA POR DEFECTO EN 20
                .execute();
        // Itera sobre cada entrada del Bundle
        for (Bundle.BundleEntryComponent entry : results.getEntry()) {
            ServiceRequest serviceRequest = (ServiceRequest) entry.getResource();

            // Verifica si el status es 'active' y si based-on es null o está vacío
            if (serviceRequest.getAuthoredOn()!=null&&serviceRequest.getOccurrence()!=null && serviceRequest.getPriority() != null && "On Hold".equals(serviceRequest.getStatus().getDisplay()) &&
                    (serviceRequest.getBasedOn() == null || serviceRequest.getBasedOn().isEmpty())) {
                JSONObject objeto = new JSONObject();
                String prioridad = serviceRequest.getPriority().getDisplay();
                String horasEstimadas = serviceRequest.getOccurrenceTiming().getRepeat().getDuration().toPlainString();
                List<String> participantes = new ArrayList<>();
                for(Coding participantesRoles : serviceRequest.getPerformerType().getCoding()){
                    participantes.add(participantesRoles.getDisplay());
                }
                objeto.accumulate("idOrden", serviceRequest.getIdPart());
                objeto.accumulate("procedimiento", serviceRequest.getCode().getConcept().getText());
                objeto.accumulate("prioridad", prioridad);
                objeto.accumulate("horasEstimadas", horasEstimadas);
                objeto.accumulate("rolesNecesarios", participantes);
                objeto.accumulate("fechaPedido", serviceRequest.getAuthoredOn());
                objeto.accumulate("paciente", obtenerPaciente(serviceRequest));
                ordenesJson.add(objeto);
            }
        }



        return new JSONArray(ordenesJson);
    }

    private String obtenerPaciente(ServiceRequest orden) {
        String nombreCompleto = "";
        Reference paciente = orden.getSubject();
        String id = paciente.getReference().substring(paciente.getReference().lastIndexOf("/") + 1);
        Patient pacienteEntidad = fhirClient.read().resource(Patient.class).withId(id).execute();
        nombreCompleto=nombreCompleto.concat(String.valueOf(pacienteEntidad.getName().get(0).getGiven().get(0)));
        nombreCompleto=nombreCompleto.concat(" ");
        nombreCompleto=nombreCompleto.concat((pacienteEntidad.getName().get(0).getFamily()));
        return nombreCompleto;
    }
}

