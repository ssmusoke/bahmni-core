package org.bahmni.module.dataintegrity.rule.impl;

import org.bahmni.module.dataintegrity.rule.RuleDefn;
import org.bahmni.module.dataintegrity.rule.RuleEngine;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.bahmni.module.dataintegrity.db.DataIntegrityResult;
import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.db.DataIntegrityService;
import org.bahmni.module.dataintegrity.rule.factory.RuleDefnsFactory;
import org.bahmni.module.dataintegrity.rule.factory.impl.PatientProgramRuleDefnsFactory;
import org.openmrs.PatientProgram;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class PatientProgramRuleEngine implements RuleEngine {
    private DataIntegrityService dataIntegrityService;
    private  RuleDefnsFactory<PatientProgram> defnsFactory;
    private Map<DataIntegrityRule, RuleDefn<PatientProgram>> rulesWithDefn;

    @Autowired
    private PatientProgramRuleEngine(DataIntegrityService dataIntegrityService,
                                     RuleDefnsFactory<PatientProgram> defnsFactory) {
        this.dataIntegrityService = dataIntegrityService;
        this.defnsFactory = defnsFactory;
    }

    @Override
    public void fireRules() {
        loadRuleDefns();
        evaluateAll();
    }

    private void loadRuleDefns() {
        List<DataIntegrityRule> diRules = dataIntegrityService.getRules();
        rulesWithDefn = defnsFactory.getRuleDefns(diRules);
    }

    private void evaluateAll() {
        List<DataIntegrityResult> results = new ArrayList<>();

        for (Map.Entry<DataIntegrityRule, RuleDefn<PatientProgram>> ruleWithDefn : rulesWithDefn.entrySet()) {
            List<DataIntegrityResult> ruleResults =
                    evaluateRule((PatientProgramRuleDefn) ruleWithDefn.getValue(), ruleWithDefn.getKey().getRuleId());

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

        for (RuleResult<PatientProgram> result : ruleResult) {
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
