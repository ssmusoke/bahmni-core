package org.bahmni.module.bahmnicore.contract.program;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.openmrs.module.emrapi.utils.CustomJsonDateSerializer;

import java.util.ArrayList;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BahmniPatientProgramData {
    private String patient;
    private String program;
    private Date dateEnrolled;
    private ArrayList<BahmniPatientProgramAttributeData> attributes;

    public BahmniPatientProgramData() {
    }

    public BahmniPatientProgramData(String patient, String program, Date dateEnrolled, ArrayList<BahmniPatientProgramAttributeData> attributes) {
        this.patient = patient;
        this.program = program;
        this.dateEnrolled = dateEnrolled;
        this.attributes = attributes;
    }

    public String getPatient() {
        return patient;
    }

    public void setPatient(String patient) {
        this.patient = patient;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    @JsonSerialize(using = CustomJsonDateSerializer.class)
    public Date getDateEnrolled() {
        return dateEnrolled;
    }

    public void setDateEnrolled(Date dateEnrolled) {
        this.dateEnrolled = dateEnrolled;
    }

    public ArrayList<BahmniPatientProgramAttributeData> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<BahmniPatientProgramAttributeData> attributes) {
        this.attributes = attributes;
    }
}

