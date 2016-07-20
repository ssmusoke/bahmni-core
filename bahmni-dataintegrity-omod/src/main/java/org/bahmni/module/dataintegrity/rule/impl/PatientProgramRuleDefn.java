package org.bahmni.module.dataintegrity.rule.impl;

import org.bahmni.module.dataintegrity.db.DataintegrityDAO;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.bahmni.module.dataintegrity.rule.RuleDefn;
import org.openmrs.PatientProgram;
import org.openmrs.api.context.Context;

import java.util.List;

public class PatientProgramRuleDefn implements RuleDefn<PatientProgram> {

    protected DataintegrityDAO dataintegrityDAO;

    public PatientProgramRuleDefn() {
        dataintegrityDAO = Context.getService(DataintegrityDAO.class);
    }

    @Override
    public List<RuleResult<PatientProgram>> evaluate() {
        return null;
    }
}