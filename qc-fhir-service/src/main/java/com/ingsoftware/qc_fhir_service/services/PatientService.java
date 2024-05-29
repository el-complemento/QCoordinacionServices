package com.ingsoftware.qc_fhir_service.services;

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
import java.util.List;

@Service
public class PatientService {
    @Autowired
    private IGenericClient fhirClient;
    private final FhirContext fhirContext = FhirContext.forR5();
    public String createPatient(String patient) {
        IParser parser =  fhirContext.newJsonParser();
        Patient nuevoPaciente = parser.parseResource(Patient.class, patient);
        MethodOutcome outcome = fhirClient.update().resource(nuevoPaciente).execute();
        return outcome.getId().getIdPart();
    }

    public Patient getPatientById(String id) {
        return fhirClient.read().resource(Patient.class).withId(id).execute();
    }
    public Bundle getAllPatients() {
        return fhirClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
    }
    public JSONArray getAllPatientsCedulas(){
        List<JSONObject> pacientesJson = new ArrayList<>();
        Bundle pacientes = fhirClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
        for (Bundle.BundleEntryComponent entry : pacientes.getEntry()) {
            Patient paciente = (Patient) entry.getResource();
            String nombreCompleto="";
            JSONObject objeto = new JSONObject();
            nombreCompleto=nombreCompleto.concat(String.valueOf(paciente.getName().get(0).getGiven().get(0)));
            nombreCompleto=nombreCompleto.concat(" ");
            nombreCompleto=nombreCompleto.concat((paciente.getName().get(0).getFamily()));
            objeto.accumulate("cedula",paciente.getIdPart());
            objeto.accumulate("nombre",nombreCompleto);
            pacientesJson.add(objeto);
        }
        return new JSONArray(pacientesJson);
    }
}