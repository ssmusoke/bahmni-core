package org.bahmni.module.dataintegrity.rule.factory.impl;

import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.rule.factory.RuleDefnLoader;
import org.bahmni.module.dataintegrity.rule.factory.RuleDefnsFactory;
import org.bahmni.module.dataintegrity.rule.RuleDefn;
import org.bahmni.module.dataintegrity.rule.impl.PatientProgramRuleDefn;
import org.openmrs.PatientProgram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientProgramRuleDefnsFactory implements RuleDefnsFactory<PatientProgram> {

    @Override
    public Map<DataIntegrityRule, RuleDefn<PatientProgram>> getRuleDefns(List<DataIntegrityRule> dataIntegrityRules) {
        Map<DataIntegrityRule, RuleDefn<PatientProgram>> rulesWithDefn = new HashMap<>();

        for (DataIntegrityRule rule : dataIntegrityRules){
            RuleDefn<PatientProgram> ruleDefn = null;
            RuleDefnLoader<PatientProgram> loader = getLoader(rule.getRuleType());

            if(loader != null)ruleDefn = loader.getRuleDefn(rule);
            if(ruleDefn != null) rulesWithDefn.put(rule, ruleDefn);
        }
        return rulesWithDefn;
    }

    private RuleDefnLoader<PatientProgram> getLoader(String ruleType){

        RuleDefnLoader<PatientProgram> loader = null;
        if("java".equals(ruleType)){
            loader = new JavaRuleDefnLoader<PatientProgram>();
        }
        else if("java".equals(ruleType)){
            //loader = new GroovyRuleDefnLoader<>();
        }
        else if("java".equals(ruleType)){
            //loader = new SQLRuleDefnLoader<>();
        }
        else{
            // invalid rule type
        }

        return loader;
    }
}
