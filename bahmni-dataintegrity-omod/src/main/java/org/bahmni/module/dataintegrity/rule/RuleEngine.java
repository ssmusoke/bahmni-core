package org.bahmni.module.dataintegrity.rule;

import org.bahmni.module.dataintegrity.db.DataIntegrityService;
import org.bahmni.module.dataintegrity.db.DataintegrityResult;
import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuleEngine<T> {

    private final DataIntegrityService dataIntegrityService;

    @Autowired
    public RuleEngine(DataIntegrityService dataIntegrityService) {
        this.dataIntegrityService = dataIntegrityService;
    }

    public void fireRules(){
        RuleResultMapper resultMapper = new RuleResultMapper();
        List<DataintegrityResult> results = new ArrayList<>();

        Map<DataintegrityRule, RuleDefn<T>> rulesWithDefns = loadRuleDefns();

        for (Map.Entry<DataintegrityRule, RuleDefn<T>> ruleWithDefn : rulesWithDefns.entrySet()) {
            List<RuleResult<T>> ruleResults = ruleWithDefn.getValue().evaluate();

            results.addAll(resultMapper.getDataintegrityResults(ruleWithDefn, ruleResults));
        }
        dataIntegrityService.saveResults(results);

    }
    private Map<DataintegrityRule, RuleDefn<T>> loadRuleDefns() {
        DefnLoader<T> loader = new DefnLoader<>();
        List<DataintegrityRule> diRules = dataIntegrityService.getRules();
        return loader.getRuleDefns(diRules);
    }
}
