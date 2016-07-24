package org.bahmni.module.dataintegrity.rule.impl;

import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.openmrs.PatientProgram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JavaRuleExample extends PatientProgramRuleDefn{

    @Override
    public List<RuleResult<PatientProgram>> evaluate() {
        List<RuleResult<PatientProgram>> ruleResults = new ArrayList<>();
        HashMap<String, List<String>> conceptValuesMap = new HashMap<>();

        conceptValuesMap.put("BMI Abnormal", Arrays.asList("True", "False"));

        List<RuleResult<PatientProgram>> results
                = dataintegrityDAO.getAllByObsAndDrugs(Arrays.asList("Gatifloxacin", "Delamanid"), conceptValuesMap);

        for(RuleResult<PatientProgram> result : results) ruleResults.add(result);

        return ruleResults;
    }
}
