package org.bahmni.module.dataintegrity.rule.impl;

import org.bahmni.module.dataintegrity.db.DataintegrityDAO;
import org.bahmni.module.dataintegrity.db.DataintegrityResult;
import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.bahmni.module.dataintegrity.db.impl.DataintegrityDAOImpl;
import org.bahmni.module.dataintegrity.rule.DataintegrityEvaluationService;
import org.bahmni.module.dataintegrity.rule.RuleDefn;
import org.bahmni.module.dataintegrity.rule.RuleDefnLoader;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.bahmni.module.dataintegrity.rule.RuleResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataintegrityEvaluationServiceImpl<T> implements DataintegrityEvaluationService<T> {

    private DataintegrityDAO dataintegrityDAO;

    @Override
    public void fireRules(){
        RuleResultMapper resultMapper = new RuleResultMapper();
        List<DataintegrityResult> results = new ArrayList<>();

        Map<DataintegrityRule, RuleDefn<T>> rulesWithDefns = loadRuleDefns();

        for (Map.Entry<DataintegrityRule, RuleDefn<T>> ruleWithDefn : rulesWithDefns.entrySet()) {
            List<RuleResult<T>> ruleResults = ruleWithDefn.getValue().evaluate();

            results.addAll(resultMapper.getDataintegrityResults(ruleWithDefn, ruleResults));
        }
        dataintegrityDAO.saveResults(results);

    }
    private Map<DataintegrityRule, RuleDefn<T>> loadRuleDefns() {
        RuleDefnLoader<T> loader = new RuleDefnLoader<>();
        List<DataintegrityRule> diRules = dataintegrityDAO.getRules();
        return loader.getRuleDefns(diRules);
    }

    public void setDataintegrityDAO(DataintegrityDAO dataintegrityDAO) {
        this.dataintegrityDAO = dataintegrityDAO;
    }

    public DataintegrityDAO getDataintegrityDAO() {
        return dataintegrityDAO;
    }
}
