package org.bahmni.module.dataintegrity.db;

import org.bahmni.module.dataintegrity.rule.impl.PatientProgramRuleResult;

import java.util.List;
import java.util.Map;

    public interface DataintegrityDAO {
    List<PatientProgramRuleResult> getAllByObsAndDrugs(List<String> drugsList, Map<String, List<String>> codedObs);
}
