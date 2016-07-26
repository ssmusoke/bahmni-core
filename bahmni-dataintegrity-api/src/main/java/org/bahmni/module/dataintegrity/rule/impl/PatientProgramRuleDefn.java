package org.bahmni.module.dataintegrity.rule.impl;

import org.bahmni.module.dataintegrity.rule.RuleDefn;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.bahmni.module.dataintegrity.db.DataintegrityDao;
import org.openmrs.PatientProgram;
import org.openmrs.api.context.Context;

import java.util.List;

public class PatientProgramRuleDefn implements RuleDefn<PatientProgram> {

    @Override
    public List<RuleResult<PatientProgram>> evaluate() {
        return null;
    }
}