package org.bahmni.module.dataintegrity.rule;

import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RuleDefnLoader {

    public Map<DataintegrityRule, RuleDefn> getRuleDefns(List<DataintegrityRule> dataintegrityRules) {
        Map<DataintegrityRule, RuleDefn> rulesWithDefn = new HashMap<>();

        for (DataintegrityRule rule : dataintegrityRules){

            if(!"java".equals(rule.getRuleType())){
                throw new IllegalArgumentException("The rule type ["+ rule.getRuleType()+"] is not supported");
            }

            RuleDefn ruleDefn = getJavaRuleInstance(rule.getRuleCode());

            if(ruleDefn != null) {
                rulesWithDefn.put(rule, ruleDefn);
            }
        }
        return rulesWithDefn;
    }

    private RuleDefn getJavaRuleInstance(String ruleCode) {
        RuleDefn ruleDefn = null;
        try {
            Class<?> ruleClass = Context.loadClass(ruleCode);
            Object o = ruleClass.newInstance();
            ruleDefn = (RuleDefn) o;
        } catch (Exception e) {
            e.printStackTrace();
            //log
        }
        return ruleDefn;
    }
}
