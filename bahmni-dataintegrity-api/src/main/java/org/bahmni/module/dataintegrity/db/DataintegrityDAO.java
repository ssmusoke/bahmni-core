package org.bahmni.module.dataintegrity.db;

import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.openmrs.PatientProgram;

import java.util.List;
import java.util.Map;

    public interface DataintegrityDao {

        List<DataintegrityRule> getRules();

        void saveResults(List<DataintegrityResult> results);

        void clearAllResults();
    }
