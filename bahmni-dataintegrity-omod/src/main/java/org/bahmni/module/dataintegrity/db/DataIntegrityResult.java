package org.bahmni.module.dataintegrity.db;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;

import java.io.Serializable;

public class DataintegrityResult extends BaseOpenmrsMetadata implements Serializable {
    private int resultId;
    private String actionURL;
    private String notes;
    private Patient patient;
    private PatientProgram patientProgram;
    private DataintegrityRule rule;

    public DataintegrityRule getRule() {
        return rule;
    }

    public void setRule(DataintegrityRule rule) {
        this.rule = rule;
    }

    public PatientProgram getPatientProgram() {
        return patientProgram;
    }

    public void setPatientProgram(PatientProgram patientProgram) {
        this.patientProgram = patientProgram;
    }


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public Integer getId() {
        return resultId;
    }

    @Override
    public void setId(Integer integer) {
        resultId = integer;
    }

    public String getActionURL() {
        return actionURL;
    }

    public void setActionURL(String actionURL) {
        this.actionURL = actionURL;
    }
}
