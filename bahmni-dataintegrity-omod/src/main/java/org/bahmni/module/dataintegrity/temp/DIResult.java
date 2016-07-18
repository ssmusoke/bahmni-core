package org.bahmni.module.dataintegrity.temp;

import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;

public class DIResult extends BaseOpenmrsObject implements Serializable {
    private int resultId;
    private int runId;
    private int ruleId;
    private int patientId;
    private int patientProgramId;
    private int obsId;
    private int encounterId;
    private int visitId;
    private String notes;

    private DIRule DIRuleByRuleId;
    private DIRun DIRunByRunId;

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
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

    public int getObsId() {
        return obsId;
    }

    public void setObsId(int obsId) {
        this.obsId = obsId;
    }

    public int getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(int encounterId) {
        this.encounterId = encounterId;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public DIRule getDIRuleByRuleId() {
        return DIRuleByRuleId;
    }

    public void setDIRuleByRuleId(DIRule DIRuleByRuleId) {
        this.DIRuleByRuleId = DIRuleByRuleId;
    }

    public DIRun getDIRunByRunId() {
        return DIRunByRunId;
    }

    public void setDIRunByRunId(DIRun DIRunByRunId) {
        this.DIRunByRunId = DIRunByRunId;
    }

    @Override
    public Integer getId() {
        return resultId;
    }

    @Override
    public void setId(Integer integer) {
        resultId = integer;
    }
}
