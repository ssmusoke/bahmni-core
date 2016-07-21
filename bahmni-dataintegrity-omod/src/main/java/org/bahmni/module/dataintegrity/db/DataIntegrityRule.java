package org.bahmni.module.dataintegrity.db;

import org.openmrs.BaseOpenmrsObject;

import java.io.Serializable;

public class DataintegrityRule extends BaseOpenmrsObject implements Serializable {
    private int ruleId;
    private String ruleName;
    private String ruleCategory;
    private String ruleType;
    private String ruleCode;


    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleCategory() {
        return ruleCategory;
    }

    public void setRuleCategory(String ruleCategory) {
        this.ruleCategory = ruleCategory;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }


    @Override
    public Integer getId() {
        return ruleId;
    }

    @Override
    public void setId(Integer integer) {
        ruleId = integer;
    }
}
