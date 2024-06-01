package com.ingsoftware.qcalgoritmo.services;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AlgoritmoService {
    private final RestTemplate restTemplate;
    private final FhirContext fhirContext = FhirContext.forR5();
    IParser parser =  fhirContext.newJsonParser();
    static final String URL_SERVICE_REQUESTS = "lb://qc-fhir-service/api/v1/service-requests/active/no-based-on";
    static final String URL_APPOINTMENTS = "lb://qc-fhir-service/api/v1/appointments";
    static final String URL_ENCOUNTERS = "lb://qc-fhir-service/api/v1/encounters";
    static final String URL_LOCATIONS = "lb://qc-fhir-service/api/v1/locations";
    static final String URL_PATIENTS = "lb://qc-fhir-service/api/v1/patients";
    static final String URL_PRACTITIONERS = "lb://qc-fhir-service/api/v1/practitioners";
    static final String URL_PRACTITIONER_ROLES = "lb://qc-fhir-service/api/v1/practitioner-roles";

    @Autowired
    public AlgoritmoService(RestTemplate restTemplate   ) {
        this.restTemplate = restTemplate;
    }


    public void processRequests() {
        LocalDateTime ahora = LocalDateTime.now();
        ResponseEntity<String> disponiblidadQuirofanos = restTemplate.getForEntity(URL_LOCATIONS,String.class);
        JSONArray quirofanos = new JSONArray(disponiblidadQuirofanos.getBody());
        List<JSONObject> listaQuirofanos =new ArrayList<>();
        for (int i = 0; i < quirofanos.length(); i++) {
            JSONObject quirofanosJSONObject = quirofanos.getJSONObject(i);
            listaQuirofanos.add(quirofanosJSONObject);
        }
        ResponseEntity<String> disponiblidadMedicos = restTemplate.getForEntity(URL_PRACTITIONER_ROLES,String.class);
        JSONArray medicos = new JSONArray(disponiblidadMedicos.getBody());
        List<JSONObject> listaMedicos =new ArrayList<>();
        for (int p = 0; p < medicos.length(); p++) {
            JSONObject medicosJSONObject = medicos.getJSONObject(p);
            listaMedicos.add(medicosJSONObject);
        }

        ResponseEntity<String> response = restTemplate.getForEntity(URL_SERVICE_REQUESTS, String.class);
        List<JSONObject> ordenesPrioridadUrgente = new ArrayList<>();
        List<JSONObject> ordenesPrioridadRoutine = new ArrayList<>();
        List<JSONObject> ordenesPrioridadASAP = new ArrayList<>();
        JSONArray ordenes = new JSONArray(response.getBody());
        for (int q = 0; q < ordenes.length(); q++) {
            JSONObject ordenesJSONObject = ordenes.getJSONObject(q);
            switch (ordenesJSONObject.get("prioridad").toString()) {
                case "Urgent":
                    ordenesPrioridadUrgente.add(ordenesJSONObject);
                    break;
                case "Routine":
                    ordenesPrioridadRoutine.add(ordenesJSONObject);
                    break;
                default:
                    ordenesPrioridadASAP.add(ordenesJSONObject);
                    break;
            }
        }

        // Ordenador de fechas
        Comparator<JSONObject> dateComparator = (o1, o2) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            try {
                Date date1 = sdf.parse(o1.getString("fechaPedido"));
                Date date2 = sdf.parse(o2.getString("fechaPedido"));
                return date1.compareTo(date2);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing date", e);
            }
        };

        // Ordenar las listas
        ordenesPrioridadUrgente.sort(dateComparator);
        ordenesPrioridadRoutine.sort(dateComparator);
        ordenesPrioridadASAP.sort(dateComparator);
        for (JSONObject orden : ordenesPrioridadUrgente) {
            Date mejorHora = new Date();
            Integer numeroQuirofanoMejorHora;
            for (int q = 0; q < listaQuirofanos.size(); q++) {
                Boolean todoAgendado = false;
                Integer horasProbadas = 0;
                while(todoAgendado!=true) {
                    LocalDateTime fechaFinal = ahora.plusHours(orden.getInt("horasEstimadas")+horasProbadas);
                    listaQuirofanos.get(q).get("notAvailableTimes");
                    if (!(isQuirófanoDisponible(listaQuirofanos.get(q),ahora.plusDays(horasProbadas),ahora.plusHours(horasProbadas).plusHours(orden.getInt("horasEstimadas"))))) continue;
                    //buscarMedicos(orden.getJSONArray("rolesNecesarios").toList(),ahora.plusHours(horasProbadas),ahora.plusHours(horasProbadas).plusHours(orden.getInt("horasEstimadas")),orden.getInt("horasEstimadas")),listaMedicos);
                    orden.get("horasEstimadas");
                    orden.get("rolesNecesarios");
                    orden.get("idOrden");
                    orden.get("prioridad");
                    horasProbadas++;
                }
            }
        }
    }
    public List<String> buscarMedicos(List<Object> rolesMedicos, LocalDateTime fechaInicio, LocalDateTime fechaFinal,List<JSONObject> disponibilidadesMedicos){
        List<String> idsMedicos = new ArrayList<>();
        for(JSONObject disponibilidad: disponibilidadesMedicos){
            if(rolesMedicos.contains(disponibilidad.get("rol")));
        }
        return idsMedicos;
    }
    public boolean isQuirófanoDisponible(JSONObject quirófano, LocalDateTime inicioDeseado, LocalDateTime finDeseado) {
        JSONArray notAvailableTimes = quirófano.getJSONArray("notAvailableTimes");
        for (int i = 0; i < notAvailableTimes.length(); i++) {
            JSONObject periodo = notAvailableTimes.getJSONObject(i);
            LocalDateTime inicioNoDisponible = LocalDateTime.parse(periodo.getString("start"));
            LocalDateTime finNoDisponible = LocalDateTime.parse(periodo.getString("end"));

            // Verificar si hay superposición
            if ((inicioDeseado.isBefore(finNoDisponible) && finDeseado.isAfter(inicioNoDisponible)) ||
                    (inicioDeseado.isAfter(inicioNoDisponible) && inicioDeseado.isBefore(finNoDisponible))||(inicioDeseado.isEqual(inicioNoDisponible) && finDeseado.isEqual(finNoDisponible))) {
                return false; // Hay superposición
            }
        }
        return true; // No hay superposiciones, el quirófano está disponible
    }


}


