package com.ingsoftware.qc_receptor_ordenes_service.model.entities;

import java.util.Date;

public class PractitionerRole {

    private String resourceType;
    private String practitionerReference;
    private String codingSystem; //"http://example.org",
    private String codingCode;  // "GP"

    private Date availableTimeDaysOfTheWeek;
    private Date availableTimeStartTime;
    private Date availableTimeEndTime;

    private Date notAvailableTimeDescription;
    private Date notAvailableTimeDuringStart;
    private Date notAvailableTimeDuringEnd;

}
