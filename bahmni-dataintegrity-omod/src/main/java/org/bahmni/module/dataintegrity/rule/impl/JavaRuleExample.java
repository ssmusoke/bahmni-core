package org.bahmni.module.dataintegrity.rule.impl;

import org.bahmni.module.dataintegrity.db.DataintegrityDAO;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.openmrs.PatientProgram;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JavaRuleExample extends PatientProgramRuleDefn{

    private DataintegrityDAO dataintegrityDAO;

    @Autowired
    public JavaRuleExample(DataintegrityDAO dataintegrityDAO){

        this.dataintegrityDAO = dataintegrityDAO;
    }

    @Override
    public List<RuleResult<PatientProgram>> evaluate() {
        List<RuleResult<PatientProgram>> ruleResults = new ArrayList<>();
        HashMap<String, List<String>> conceptValuesMap = new HashMap<>();

        conceptValuesMap.put("EOT, Outcome", Arrays.asList("Completed", "Not Evaluated"));

        List<PatientProgramRuleResult> results
                = dataintegrityDAO.getAllByObsAndDrugs(Arrays.asList("Bedaquiline", "Delamanid"), conceptValuesMap);

        for(PatientProgramRuleResult result : results) ruleResults.add(result);

        return ruleResults;
    }
}
