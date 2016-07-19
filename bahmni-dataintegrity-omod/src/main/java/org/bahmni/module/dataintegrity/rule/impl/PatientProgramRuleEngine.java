package org.bahmni.module.dataintegrity.rule.impl;

import org.bahmni.module.dataintegrity.rule.RuleEngine;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.bahmni.module.dataintegrity.db.DataIntegrityResult;
import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.db.DataIntegrityService;
import org.openmrs.PatientProgram;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class PatientProgramRuleEngine implements RuleEngine {

    private DataIntegrityService dataIntegrityService;
    private Map<DataIntegrityRule, PatientProgramRuleDefn> rulesWithDefn;

    @Autowired
    private PatientProgramRuleEngine(DataIntegrityService dataIntegrityService) {
        this.dataIntegrityService = dataIntegrityService;
    }

    @Override
    public void fireRules() {
        loadRuleDefns();
        evaluateAll();
    }

    private void loadRuleDefns() {
        HashMap<DataIntegrityRule, PatientProgramRuleDefn> instances = new HashMap<>();
        List<DataIntegrityRule> diRules = dataIntegrityService.getRules();

        rulesWithDefn = null;
    }

    private void evaluateAll() {

        List<DataIntegrityResult> results = new ArrayList<>();

        for(Map.Entry<DataIntegrityRule, PatientProgramRuleDefn> ruleWithDefn : rulesWithDefn.entrySet()){
            List<DataIntegrityResult> ruleResults =
                    evaluateRule(ruleWithDefn.getValue(), ruleWithDefn.getKey().getRuleId());
            results.addAll(ruleResults);
        }

        dataIntegrityService.saveResults(results);
    }

    private List<DataIntegrityResult> evaluateRule(PatientProgramRuleDefn ruleDefn, int ruleId) {

        List<RuleResult<PatientProgram>> ruleResult = ruleDefn.evaluate();

        return RuleResultToDataintegrityResult(ruleId, ruleResult);
    }

    private List<DataIntegrityResult> RuleResultToDataintegrityResult(int ruleId, List<RuleResult<PatientProgram>> ruleResult) {
        List<DataIntegrityResult> results = new ArrayList<>();
        for (RuleResult<PatientProgram> result : ruleResult){
            DataIntegrityResult diResult = new DataIntegrityResult();

            diResult.setPatientProgramId(result.getEntity().getPatientProgramId());
            diResult.setRuleId(ruleId);
            diResult.setNotes(result.getNotes());
            diResult.setActionURL(result.getActionUrl());

            results.add(diResult);
        }
        return results;
    }
}
