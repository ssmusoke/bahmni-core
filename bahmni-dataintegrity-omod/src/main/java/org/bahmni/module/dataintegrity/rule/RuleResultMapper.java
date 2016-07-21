package org.bahmni.module.dataintegrity.rule;

import org.bahmni.module.dataintegrity.db.DataintegrityResult;
import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.bahmni.module.dataintegrity.rule.impl.PatientProgramRuleDefn;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class RuleResultMapper<T> {
    public List<DataintegrityResult> getDataintegrityResults(Entry<DataintegrityRule, RuleDefn<T>> ruleWithDefn, List<RuleResult<T>> ruleResults) {
        List<DataintegrityResult> results = new ArrayList<>();
        for (RuleResult<T> result : ruleResults) {
            DataintegrityResult diResult = new DataintegrityResult();

            if(ruleWithDefn.getValue() instanceof PatientProgramRuleDefn) {
                PatientProgram pp = new PatientProgram();
                pp.setId((Integer) result.getEntity());
                diResult.setPatientProgram(pp);
            }
            diResult.setRule(ruleWithDefn.getKey());
            diResult.setNotes(result.getNotes());
            diResult.setActionUrl(result.getActionUrl());

            results.add(diResult);
        }
        return results;
    }
}
