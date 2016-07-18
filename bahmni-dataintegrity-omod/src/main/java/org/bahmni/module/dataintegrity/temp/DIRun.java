package org.bahmni.module.dataintegrity.temp;


import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;

public class DIRun extends BaseOpenmrsObject implements Serializable {
    private int runId;


    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    @Override
    public Integer getId() {
        return runId;
    }

    @Override
    public void setId(Integer integer) {
        runId = integer;
    }
}
