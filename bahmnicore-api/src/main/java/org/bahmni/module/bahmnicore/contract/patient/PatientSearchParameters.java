package org.bahmni.module.bahmnicore.contract.patient;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.webservices.rest.web.RequestContext;

import java.util.Map;

public class PatientSearchParameters {
    private Boolean filterPatientsByLocation;
    private String identifier;
    private String name;
    private String addressFieldName;
    private String addressFieldValue;
    private Integer start;
    private Integer length;
    private String customAttribute;
    private String[] patientAttributes;
    private String identifierPrefix;
    private String programAttributeFieldValue;
    private String programAttributeFieldName;
    private String loginLocationUuid;
    private String[] addressSearchResultFields;
    private String[] patientSearchResultFields;

    public PatientSearchParameters(RequestContext context) {
        String query = context.getParameter("q");
        String identifier = context.getParameter("identifier");
        String identifierPrefix = context.getParameter("identifierPrefix");
        if (identifierPrefix == null)
            identifierPrefix = "";
        if (identifier != null) {
            this.setIdentifier(identifier);
            this.setIdentifierPrefix(identifierPrefix);
        } else if (query != null) {
            if (query.matches(".*\\d+.*")) {
                this.setIdentifier(query);
                this.setIdentifierPrefix("");
            } else {
                this.setName(query);
            }
        }
        this.setStart(context.getStartIndex());
        this.setLength(context.getLimit());
        this.setCustomAttribute(context.getParameter("customAttribute"));
        String addressFieldNameFromRequest = context.getParameter("addressFieldName");
        if  (StringUtils.isNotEmpty(addressFieldNameFromRequest)){
            this.setAddressFieldName(addressFieldNameFromRequest);
        } else {
            this.setAddressFieldName("city_village");
        }
        this.setAddressFieldValue(context.getParameter("addressFieldValue"));
        Map parameterMap = context.getRequest().getParameterMap();
        this.setAddressSearchResultFields((String[]) parameterMap.get("addressSearchResultsConfig"));
        this.setPatientSearchResultFields((String[]) parameterMap.get("patientSearchResultsConfig"));
        this.setPatientAttributes((String[]) parameterMap.get("patientAttributes"));
        this.setProgramAttributeFieldValue(context.getParameter("programAttributeFieldValue"));
        this.setProgramAttributeFieldName(context.getParameter("programAttributeFieldName"));
        this.setFilterPatientsByLocation(Boolean.valueOf(context.getParameter("filterPatientsByLocation")));
        this.setLoginLocationUuid(context.getParameter("loginLocationUuid"));
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierPrefix() {return identifierPrefix;}

    public void setIdentifierPrefix(String identifierPrefix) {this.identifierPrefix = identifierPrefix;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressFieldName() {
        return addressFieldName;
    }

    public void setAddressFieldName(String addressFieldName) {
        this.addressFieldName = addressFieldName;
    }

    public String getAddressFieldValue() {
        return addressFieldValue;
    }

    public void setAddressFieldValue(String addressFieldValue) {
        this.addressFieldValue = addressFieldValue;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getCustomAttribute() {
        return customAttribute;
    }

    public void setCustomAttribute(String customAttribute) {
        this.customAttribute = customAttribute;
    }

    public String[] getPatientAttributes() {
        return patientAttributes;
    }

    public void setPatientAttributes(String[] patientAttributes) {
        this.patientAttributes = patientAttributes;
    }

    public String getProgramAttributeFieldValue() {
        return programAttributeFieldValue;
    }

    public void setProgramAttributeFieldValue(String programAttributeFieldValue) {
        this.programAttributeFieldValue = programAttributeFieldValue;
    }

    public String getProgramAttributeFieldName() {
        return programAttributeFieldName;
    }

    public void setProgramAttributeFieldName(String programAttributeFieldName) {
        this.programAttributeFieldName = programAttributeFieldName;
    }

    public String[] getAddressSearchResultFields() {
        return addressSearchResultFields;
    }

    public void setAddressSearchResultFields(String[] addressSearchResultFields) {
        this.addressSearchResultFields = addressSearchResultFields;
    }

    public String[] getPatientSearchResultFields() {
        return patientSearchResultFields;
    }

    public void setPatientSearchResultFields(String[] patientSearchResultFields) {
        this.patientSearchResultFields = patientSearchResultFields;
    }

    public String getLoginLocationUuid() {
        return loginLocationUuid;
    }

    public void setLoginLocationUuid(String loginLocationUuid) {
        this.loginLocationUuid = loginLocationUuid;
    }

    public Boolean getFilterPatientsByLocation() {
        return filterPatientsByLocation;
    }

    public void setFilterPatientsByLocation(Boolean filterPatientsByLocation) {
        this.filterPatientsByLocation = filterPatientsByLocation;
    }
}
