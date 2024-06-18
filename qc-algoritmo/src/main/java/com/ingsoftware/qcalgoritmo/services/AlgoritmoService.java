package com.ingsoftware.qcalgoritmo.services;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import lombok.Getter;
import lombok.Setter;
import org.hl7.fhir.r5.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AlgoritmoService {
    private final RestTemplate restTemplate;
    private final FhirContext fhirContext = FhirContext.forR5();
    IParser parser =  fhirContext.newJsonParser();
    static final String URL_SERVICE_REQUESTS = "lb://qc-fhir-service/api/v1/service-requests/active/no-based-on";
    static final String URL_APPOINTMENTS = "lb://qc-fhir-service/api/v1/appointments";
    static final String URL_LOCATIONS = "lb://qc-fhir-service/api/v1/locations";
    static final String URL_PRACTITIONER_ROLES = "lb://qc-fhir-service/api/v1/practitioner-roles";

    @Autowired
    public AlgoritmoService(RestTemplate restTemplate   ) {
        this.restTemplate = restTemplate;
    }


    public void processRequests() {
        LocalDateTime ahora = LocalDateTime.now();
        ResponseEntity<String> disponiblidadQuirofanos = restTemplate.getForEntity(URL_LOCATIONS, String.class);
        JSONArray quirofanos = new JSONArray(disponiblidadQuirofanos.getBody());
        List<JSONObject> listaQuirofanos = new ArrayList<>();
        for (int i = 0; i < quirofanos.length(); i++) {
            JSONObject quirofanosJSONObject = quirofanos.getJSONObject(i);
            listaQuirofanos.add(quirofanosJSONObject);
        }
        ResponseEntity<String> disponiblidadMedicos = restTemplate.getForEntity(URL_PRACTITIONER_ROLES, String.class);
        JSONArray medicos = new JSONArray(disponiblidadMedicos.getBody());
        List<JSONObject> listaMedicos = new ArrayList<>();
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
            switch (ordenesJSONObject.getString("prioridad")) {
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
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
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
        processOrderList(ordenesPrioridadASAP, listaQuirofanos, listaMedicos);
        processOrderList(ordenesPrioridadUrgente, listaQuirofanos, listaMedicos);
        processOrderList(ordenesPrioridadRoutine, listaQuirofanos, listaMedicos);
    }
    public void processOrderList(List<JSONObject> ordenes, List<JSONObject> listaQuirofanos, List<JSONObject> listaMedicos) {
        LocalDateTime ahora = LocalDateTime.now();
        if(ordenes.isEmpty()) return;
        for (JSONObject orden : ordenes) {
            LocalDateTime mejorHora = null;
            String numeroQuirofanoMejorHora = "";
            List<String> idsMedicosConMejorHora = new ArrayList<>();
            for (int q = 0; q < listaQuirofanos.size(); q++) {
                boolean todoAgendado = false;
                int horasProbadas = -1;
                while (!todoAgendado) {
                    horasProbadas++;
                    if (!isQuirófanoDisponible(listaQuirofanos.get(q),
                            ahora.plusDays(horasProbadas),
                            ahora.plusHours(horasProbadas).plusHours(orden.getInt("horasEstimadas")))) {
                        continue;
                    }
                    List<String> idMedicos = buscarMedicos(orden.getJSONArray("rolesNecesarios").toList(),
                            ahora.plusHours(horasProbadas),
                            ahora.plusHours(horasProbadas).plusHours(orden.getInt("horasEstimadas")), listaMedicos);
                    if (idMedicos.isEmpty()) continue;
                    if (mejorHora == null || ahora.plusHours(horasProbadas).isBefore(mejorHora)) {
                        mejorHora = ahora.plusHours(horasProbadas);
                        numeroQuirofanoMejorHora = listaQuirofanos.get(q).getString("id");
                        idsMedicosConMejorHora = idMedicos;
                    }
                    todoAgendado = true;
                }
            }
            updateLocationAvailability(numeroQuirofanoMejorHora, mejorHora, mejorHora.plusHours(orden.getInt("horasEstimadas")));
            for (String id : idsMedicosConMejorHora) {
                updateMedicosAvailability(id, mejorHora, mejorHora.plusHours(orden.getInt("horasEstimadas")));
            }
            createAppointment(orden.getString("idOrden"), orden.getString("prioridad"), orden.getString("paciente"),
                    numeroQuirofanoMejorHora, idsMedicosConMejorHora, mejorHora, orden.getInt("horasEstimadas"));
            cambiarEstadoServiceRequest(orden.getString("idOrden"));
        }
    }

    private void cambiarEstadoServiceRequest(String idOrden) {
        ResponseEntity<String> disponiblidadQuirofanos = restTemplate.getForEntity("lb://qc-fhir-service/api/v1/service-requests/draft/"+idOrden, String.class);
    }

    public void createAppointment(String idOrden, String prioridad,String paciente,String idQuirofano ,List<String> idsMedicosConMejorHora, LocalDateTime mejorHora, Integer horasEstimadas) {
        Appointment nuevoAppointment = new Appointment();
        Reference referenciaBasedOn = new Reference();
        Reference idPaciente = new Reference();
        referenciaBasedOn.setReference("ServiceRequest/"+idOrden);
        idPaciente.setReference(paciente);
        CodeableConcept prioridadAPoner = new CodeableConcept();
        Coding codigoAPoner = new Coding();
        codigoAPoner.setDisplay(prioridad);
        prioridadAPoner.addCoding(codigoAPoner);
        //configuro el nuevo appointment
        nuevoAppointment.addParticipant().setActor(new Reference("Location/"+idQuirofano));
        nuevoAppointment.addBasedOn(referenciaBasedOn);
        nuevoAppointment.setStatus(Appointment.AppointmentStatus.PROPOSED);
        nuevoAppointment.setPriority(prioridadAPoner);
        nuevoAppointment.setSubject(idPaciente);
        nuevoAppointment.setStart(convertToDate(mejorHora));
        nuevoAppointment.setEnd(convertToDate(mejorHora.plusHours(horasEstimadas)));
        //gettear medicos
        for(String ids: idsMedicosConMejorHora){
            nuevoAppointment.addParticipant().setActor(new Reference("Practitioner/"+ids));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String appointmentString = parser.encodeToString(nuevoAppointment);
        HttpEntity<String> request = new HttpEntity<>(appointmentString, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(URL_APPOINTMENTS, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Appointment created successfully");
        } else {
            System.out.println("Failed to create appointment: " + response.getBody());
        }

    }
    public Date convertToDate(LocalDateTime localDateTime) {
        // Considerando que la fecha y hora son en zona UTC
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public List<String> buscarMedicos(List<Object> rolesMedicos, LocalDateTime fechaInicio, LocalDateTime fechaFinal,List<JSONObject> disponibilidadesMedicos){
        List<String> idsMedicos = new ArrayList<>();
        HashMap<String,Integer> cantidadesRoles = new HashMap<>();
        for (Object ids: rolesMedicos){
            cantidadesRoles.put(ids.toString(),cantidadesRoles.getOrDefault(ids.toString()+1,1));
        }
        for(JSONObject medico: disponibilidadesMedicos){
            if(medico.has("rol")&&(cantidadesRoles.get(medico.get("rol").toString()) == null|| cantidadesRoles.get(medico.get("rol").toString())==0 )) continue;
            if(!isMedicoDisponible(medico.getJSONArray("disponibilidad"),fechaInicio,fechaFinal)) continue;
            idsMedicos.add(medico.getString("idPractitioner"));
            cantidadesRoles.put(medico.get("rol").toString(),(cantidadesRoles.get(medico.get("rol").toString())-1));
        }
        if(idsMedicos.size()==rolesMedicos.size()) return idsMedicos;
        return new ArrayList<>();
    }
    public boolean isMedicoDisponible(JSONArray disponiblidad, LocalDateTime fechaInicio, LocalDateTime fechaFinal) {
        // Crear un DateTimeFormatter que coincida con el formato de la cadena de fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        for (int i = 0; i < disponiblidad.length(); i++) {
            JSONObject availability = disponiblidad.getJSONObject(i);

            String diasDeLaSemana = availability.getString("daysOfWeek");
            String dayToCheck = fechaInicio.getDayOfWeek().toString().toLowerCase().substring(0, 3);

            // Dividir la cadena original en partes basado en ","
            String[] diasArray = diasDeLaSemana.split(",");

            // Crear una nueva lista para almacenar los días limpios
            List<String> diasLimpios = new ArrayList<>();

            // Limpiar cada elemento del arreglo
            for (String dia : diasArray) {
                dia = dia.trim(); // Eliminar espacios en blanco iniciales y finales
                dia = dia.replace("Enumeration[", ""); // Eliminar el prefijo
                dia = dia.replace("]", ""); // Eliminar el sufijo
                diasLimpios.add(dia.toLowerCase()); // Convertir a minúscula y agregar a la lista
            }
            diasLimpios.set(0,diasLimpios.get(0).replace("[",""));

            // Comprobación de la disponibilidad
            if (!diasLimpios.contains(dayToCheck)) {
                return false; // No disponible en ese día, pasa a la siguiente disponibilidad
            }
            if (availability.has("availableStartTime") && availability.has("availableEndTime")) {
                LocalTime availableStart = LocalTime.parse(availability.getString("availableStartTime"), timeFormatter);
                LocalTime availableEnd = LocalTime.parse(availability.getString("availableEndTime"), timeFormatter);
                if(availableEnd.equals("00:00")) availableEnd = LocalTime.parse("23:59");

                // Aseguramos que la hora de inicio y fin de la cita esté dentro de las horas disponibles
                if (fechaInicio.toLocalTime().isBefore(availableStart) || fechaFinal.toLocalTime().isAfter(availableEnd)) {
                    return false; // Las horas solicitadas no están dentro del rango de horas disponibles
                }
            }
            if (availability.has("notAvailableTime")) {
                JSONArray notAvailable = availability.getJSONArray("notAvailableTime");
                for (int j = 0; j < notAvailable.length(); j++) {
                    JSONObject unavailableTime = notAvailable.getJSONObject(j);
                    JSONObject duringObject = unavailableTime.getJSONObject("during");
                    String startString = duringObject.getString("start");
                    LocalDateTime unavailableStart = LocalDateTime.parse(startString,formatter);
                    JSONObject duringObjectEnd = unavailableTime.getJSONObject("during");
                    String endString = duringObject.getString("end");
                    LocalDateTime unavailableEnd = LocalDateTime.parse(endString,formatter);

                    // Check if desired appointment overlaps with unavailable time
                    if ((fechaInicio.isBefore(unavailableEnd) || fechaInicio.isEqual(unavailableEnd)) && (fechaFinal.isAfter(unavailableStart) || fechaFinal.isEqual(unavailableStart))) {
                        return false; // Appointment overlaps with unavailable time
                    }
                }
            }
        }

        // No matching availability found
        return true;
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
    private static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, FORMATTER);
        } catch (Exception e) {
            System.err.println("Error parsing date: " + dateTimeStr + ", error: " + e.getMessage());
            return null; // or handle more appropriately
        }
    }

    public boolean isQuirófanoDisponible(JSONObject quirófano, LocalDateTime inicioDeseado, LocalDateTime finDeseado) {
        JSONArray notAvailableTimes = quirófano.getJSONArray("notAvailableTimes");

        for (int i = 0; i < notAvailableTimes.length(); i++) {
            JSONObject periodo = notAvailableTimes.getJSONObject(i);
            LocalDateTime inicioNoDisponible = parseLocalDateTime(periodo.getString("start"));
            LocalDateTime finNoDisponible = parseLocalDateTime(periodo.getString("end"));

            // Verificar si hay superposición
            if ((inicioDeseado.isBefore(finNoDisponible) && finDeseado.isAfter(inicioNoDisponible)) ||
                    (inicioDeseado.isAfter(inicioNoDisponible) && inicioDeseado.isBefore(finNoDisponible))||(inicioDeseado.isEqual(inicioNoDisponible) && finDeseado.isEqual(finNoDisponible))) {
                return false; // Hay superposición
            }
        }
        return true; // No hay superposiciones, el quirófano está disponible
    }
    @Setter
    @Getter
    public static class UpdateAvailabilityRequest {
        private LocalDateTime start;
        private LocalDateTime end;

    }
    public void updateLocationAvailability(String locationId, LocalDateTime start, LocalDateTime end) {
        // URL del endpoint a llamar
        String url = "lb://qc-fhir-service/api/v1/locations/" + locationId + "/updateAvailability";

        // Crear el objeto de solicitud
        UpdateAvailabilityRequest request = new UpdateAvailabilityRequest();
        request.setStart(start);
        request.setEnd(end);

        // Convertir objeto de solicitud a HttpEntity con headers adecuados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdateAvailabilityRequest> entity = new HttpEntity<>(request, headers);



        // Enviar la solicitud POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // Manejar la respuesta
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Location availability updated successfully");
        } else {
            System.out.println("Failed to update location availability: " + response.getBody());
        }
    }
    public void updateMedicosAvailability(String idMedico, LocalDateTime start, LocalDateTime end) {
        // URL del endpoint a llamar
        String url = "lb://qc-fhir-service/api/v1/practitioner-roles/" + idMedico + "/updateAvailability";

        // Crear el objeto de solicitud
        UpdateAvailabilityRequest request = new UpdateAvailabilityRequest();
        request.setStart(start);
        request.setEnd(end);

        // Convertir objeto de solicitud a HttpEntity con headers adecuados
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdateAvailabilityRequest> entity = new HttpEntity<>(request, headers);

        // Enviar la solicitud POST
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // Manejar la respuesta
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Medico availability updated successfully");
        } else {
            System.out.println("Failed to update medico availability: " + response.getBody());
        }
    }
}