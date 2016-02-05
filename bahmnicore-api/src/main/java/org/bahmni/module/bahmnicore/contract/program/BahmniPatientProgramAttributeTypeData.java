package org.bahmni.module.bahmnicore.contract.program;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BahmniPatientProgramAttributeTypeData {
    private String uuid;

    public BahmniPatientProgramAttributeTypeData() {
    }

    public BahmniPatientProgramAttributeTypeData(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
