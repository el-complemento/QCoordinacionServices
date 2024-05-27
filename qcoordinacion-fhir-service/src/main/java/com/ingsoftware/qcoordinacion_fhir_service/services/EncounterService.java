    package com.ingsoftware.qcoordinacion_fhir_service.services;

    import ca.uhn.fhir.context.FhirContext;
    import ca.uhn.fhir.parser.IParser;
    import ca.uhn.fhir.rest.client.api.IGenericClient;
    import org.hl7.fhir.r5.model.*;
    import ca.uhn.fhir.rest.api.MethodOutcome;
    import org.json.JSONArray;
    import org.json.JSONObject;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.List;

    @Service
    public class EncounterService {
        @Autowired
        private IGenericClient fhirClient;
        private final FhirContext fhirContext = FhirContext.forR5();
        public String createEncounter(String encounter) {
            IParser parser = fhirContext.newJsonParser();
            Encounter nuevoEncounter = parser.parseResource(Encounter.class, encounter);
            MethodOutcome outcome = fhirClient.create().resource(nuevoEncounter).execute();
            return outcome.getId().getIdPart();
        }
        public Encounter getEncounterbyId(String idEncounter){
            return fhirClient.read().resource(Encounter.class).withId(idEncounter).execute();
        }
        public void setEncounterAsCompleted(String id){
            getEncounterbyId(id).setStatus(Enumerations.EncounterStatus.COMPLETED);
        }
        public void deleteEncounterById(String encounterId) {
            try {
                // Crear el IdType con el tipo de recurso y el ID
                IdType resourceId = new IdType("Encounter", encounterId);

                // Eliminar el recurso Encounter especificado
                fhirClient.delete().resourceById(resourceId).execute();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public JSONArray getAllEncounters() {
            List<JSONObject> cirugiasJson = new ArrayList<>();

            Bundle cirugias = fhirClient.search().forResource(Encounter.class)
                    .where(Encounter.STATUS.exactly().code("planned"))
                    .returnBundle(Bundle.class).execute();

            for (Bundle.BundleEntryComponent entry : cirugias.getEntry()) {
                JSONObject objeto = new JSONObject();
                Encounter encounter = (Encounter) entry.getResource();
                String idOrden = encounter.getBasedOn().get(0).getReference().split("/")[1];
                ServiceRequest ordenAsociada = obtenerOrden(idOrden);
                String prioridad = String.valueOf(encounter.getPriority().getCoding().get(0).getDisplay());

                objeto.accumulate("title", ordenAsociada.getCode().getConcept().getText());
                objeto.accumulate("start", encounter.getPlannedStartDate());
                objeto.accumulate("end", encounter.getPlannedEndDate());
                objeto.accumulate("prioridad", prioridad);
                objeto.accumulate("color", obtenerColorPorPrioridad(prioridad));
                objeto.accumulate("doctores", obtenerDoctores(encounter));
                objeto.accumulate("paciente", obtenerPaciente(encounter));
                objeto.accumulate("idOrden", idOrden);
                objeto.accumulate("quirofano",  encounter.getLocation().get(0).getLocation().getReference().split("/")[1]);

                cirugiasJson.add(objeto);
            }

            return new JSONArray(cirugiasJson);
        }

        private ServiceRequest obtenerOrden(String idOrden) {
            return fhirClient.read().resource(ServiceRequest.class).withId(idOrden).execute();
        }

        private List<String> obtenerDoctores(Encounter encounter) {
            List<String> nombres = new ArrayList<>();
            for (Encounter.EncounterParticipantComponent participant : encounter.getParticipant()) {
                String nombreCompleto="";
                Reference ref = participant.getActor();
                if (ref != null && ref.getReference() != null) {
                    String id = ref.getReference().substring(ref.getReference().lastIndexOf("/") + 1);
                    Practitioner practitioner = fhirClient.read().resource(Practitioner.class).withId(id).execute();
                    nombreCompleto=nombreCompleto.concat(String.valueOf(practitioner.getName().get(0).getGiven().get(0)));
                    nombreCompleto=nombreCompleto.concat(" ");
                    nombreCompleto=nombreCompleto.concat((practitioner.getName().get(0).getFamily()));
                    nombres.add(nombreCompleto);
                }
            }
            return nombres;
        }

        private String obtenerPaciente(Encounter encounter) {
            String nombreCompleto = "";
            Reference paciente = encounter.getSubject();
            String id = paciente.getReference().substring(paciente.getReference().lastIndexOf("/") + 1);
            Patient pacienteEntidad = fhirClient.read().resource(Patient.class).withId(id).execute();
            nombreCompleto=nombreCompleto.concat(String.valueOf(pacienteEntidad.getName().get(0).getGiven().get(0)));
            nombreCompleto=nombreCompleto.concat(" ");
            nombreCompleto=nombreCompleto.concat((pacienteEntidad.getName().get(0).getFamily()));
            return nombreCompleto;
        }

        private String obtenerColorPorPrioridad(String prioridad) {
            String color = prioridad;

            if(prioridad.equals("ASAP") || prioridad.equals("emergency")){
                color = "high";
            }

            if( prioridad.equals("urgent") || prioridad.equals("elective")){
                color = "normal";
            }

            if(prioridad.equals("routine")){
                color = "low";
            }

            return color;
        }
    }
