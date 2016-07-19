package org.bahmni.module.dataintegrity.factory.impl;

import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.factory.RuleDefnLoader;
import org.bahmni.module.dataintegrity.factory.RuleDefnsFactory;
import org.bahmni.module.dataintegrity.rule.RuleDefn;
import org.openmrs.PatientProgram;

import java.util.ArrayList;
import java.util.List;

public class PatientProgramRuleDefnsFactory implements RuleDefnsFactory<PatientProgram> {

    @Override
    public List<RuleDefn<PatientProgram>> getRuleDefns(List<DataIntegrityRule> dataIntegrityRules) {
        List<RuleDefn<PatientProgram>> ruleDefns = new ArrayList<>();

        for (DataIntegrityRule rule : dataIntegrityRules){
            RuleDefn<PatientProgram> ruleDefn = null;
            RuleDefnLoader<PatientProgram> loader = getLoader(rule.getRuleType());

            if(loader != null)ruleDefn = loader.getRuleDefn(rule);
            if(ruleDefn != null) ruleDefns.add(ruleDefn);
        }
        return ruleDefns;
    }

    private RuleDefnLoader<PatientProgram> getLoader(String ruleType){

        RuleDefnLoader<PatientProgram> loader = null;
        if("java".equals(ruleType)){
            loader = new JavaRuleDefnLoader<>();
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
