package com.ingsoftware.qc_fhir_service.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r5.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class LocationService {
    @Autowired
    private IGenericClient fhirClient;
    private final FhirContext fhirContext = FhirContext.forR5();

    public String createQuirofano(String quirofano) {
        IParser parser =  fhirContext.newJsonParser();
        Location nuevoQuirofano = parser.parseResource(Location.class, quirofano);
        MethodOutcome outcome = fhirClient.create().resource(nuevoQuirofano).execute();
        return outcome.getId().getIdPart();
    }
    

    public JSONArray getAllQuirofanosDisponibilidad() {
        IParser parser = fhirContext.newJsonParser();

        // Realizar la búsqueda para obtener todos los recursos Location
        Bundle results = fhirClient.search().forResource(Location.class)
                .returnBundle(Bundle.class)
                .execute();

        JSONArray quirofanosConDisponibilidad = new JSONArray();

        // Iterar sobre cada entrada en el bundle
        for (Bundle.BundleEntryComponent entry : results.getEntry()) {
            if (entry.getResource() instanceof Location) {
                Location quirofano = (Location) entry.getResource();
                String id = quirofano.getIdElement().getIdPart();
                JSONObject quirofanoJson = new JSONObject();
                quirofanoJson.put("id", id);

                // Crear un JSONArray para los tiempos de no disponibilidad
                JSONArray notAvailableTimesJsonArray = new JSONArray();
                if (quirofano.getHoursOfOperation() != null && !quirofano.getHoursOfOperation().isEmpty() &&
                        quirofano.getHoursOfOperation().get(0).getNotAvailableTime() != null) {
                    List<Availability.AvailabilityNotAvailableTimeComponent> notAvailableTimes =
                            quirofano.getHoursOfOperation().get(0).getNotAvailableTime();

                    // Llenar el JSONArray con los tiempos de no disponibilidad
                    for (Availability.AvailabilityNotAvailableTimeComponent time : notAvailableTimes) {
                        JSONObject notAvailableTimeJson = new JSONObject();
                        notAvailableTimeJson.put("start", time.getDuring().getStart());
                        notAvailableTimeJson.put("end", time.getDuring().getEnd());
                        notAvailableTimesJsonArray.put(notAvailableTimeJson);
                    }
                }
                quirofanoJson.put("notAvailableTimes", notAvailableTimesJsonArray);

                quirofanosConDisponibilidad.put(quirofanoJson);
            }
        }

        return quirofanosConDisponibilidad;
    }

    public void deleteQuirofano(String quirofanoId) {
        try {
            // Crear el IdType con el tipo de recurso y el ID
            IdType resourceId = new IdType("Location", quirofanoId);

            // Eliminar el recurso Encounter especificado
            fhirClient.delete().resourceById(resourceId).execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLocationAvailability(String locationId, LocalDateTime start, LocalDateTime end) {
        try {
            // Create an IdType for the operating room resource
            IdType resourceId = new IdType("Location", locationId);

            // Use PATCH operation to update the existing resource
            Location existingLocation = fhirClient.read().resource(Location.class).withId(resourceId).execute();

            if (existingLocation == null) {
                throw new IllegalArgumentException("Location not found");
            }

            // Check if HoursOfOperation exists, create if not
            if (existingLocation.getHoursOfOperation() == null || existingLocation.getHoursOfOperation().isEmpty()) {
                existingLocation.addHoursOfOperation();
            }

            // Get the first (and likely only) HoursOfOperation element
            Availability hoursOfOperation = existingLocation.getHoursOfOperation().get(0);
            Date startEnDate = convertToDate(start);
            Date endEnDate = convertToDate(end);
            // Create a new NotAvailableTimeComponent with start and end times
            Availability.AvailabilityNotAvailableTimeComponent notAvailableTime =
                    new Availability.AvailabilityNotAvailableTimeComponent();
            notAvailableTime.setDuring(new Period().setStart(startEnDate).setEnd(endEnDate));

            // Append the new NotAvailableTimeComponent to the existing list
            hoursOfOperation.addNotAvailableTime(notAvailableTime);

            // Update the Location resource with the modified HoursOfOperation
            fhirClient.update().resource(existingLocation).execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating location availability: " + e.getMessage());
        }
    }
    public Date convertToDate(LocalDateTime localDateTime) {
        // Considerando que la fecha y hora son en zona UTC
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
