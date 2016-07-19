package org.bahmni.module.dataintegrity.db;

import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;

public class DataIntegrityResult extends BaseOpenmrsObject implements Serializable {
    private int resultId;
    private int ruleId;
    private int patientId;
    private int patientProgramId;
    private String actionURL;
    private String notes;

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getPatientProgramId() {
        return patientProgramId;
    }

    public void setPatientProgramId(int patientProgramId) {
        this.patientProgramId = patientProgramId;
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
