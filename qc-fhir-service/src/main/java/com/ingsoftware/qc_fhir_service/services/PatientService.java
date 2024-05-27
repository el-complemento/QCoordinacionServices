package com.ingsoftware.qc_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r5.model.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
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

    public  HumanName getPatientNombreById(String id) {
        Patient paciente = fhirClient.read().resource(Patient.class).withId(id).execute();
        List<HumanName> names = paciente.getName();
        return paciente.getNameFirstRep();
    }
    public Patient getPatientById(String id) {
        return fhirClient.read().resource(Patient.class).withId(id).execute();
    }
    public Bundle getAllPatients() {
        return fhirClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
    }
    public List<String> getAllPatientsCedulas(){
        List<String> cedulas = new ArrayList<>();
        Bundle pacientes = fhirClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
        for (Bundle.BundleEntryComponent entry : pacientes.getEntry()) {
            cedulas.add(entry.getResource().getIdPart());
        }
        return cedulas;
    }
}

