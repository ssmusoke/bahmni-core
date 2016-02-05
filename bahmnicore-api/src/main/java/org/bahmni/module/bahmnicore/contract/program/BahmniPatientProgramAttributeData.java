package org.bahmni.module.bahmnicore.contract.program;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BahmniPatientProgramAttributeData {
    private BahmniPatientProgramAttributeTypeData attributeType;
    private String value;

    public BahmniPatientProgramAttributeData() {
    }

    public BahmniPatientProgramAttributeData(BahmniPatientProgramAttributeTypeData attributeType, String value) {
        this.attributeType = attributeType;
        this.value = value;
    }

    public BahmniPatientProgramAttributeTypeData getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(BahmniPatientProgramAttributeTypeData attributeType) {
        this.attributeType = attributeType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
