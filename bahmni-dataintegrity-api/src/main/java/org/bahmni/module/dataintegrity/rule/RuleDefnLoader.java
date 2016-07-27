package org.bahmni.module.dataintegrity.rule;

import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.openmrs.util.OpenmrsClassLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleDefnLoader<T> {

    public Map<DataintegrityRule, RuleDefn<T>> getRuleDefns(List<DataintegrityRule> dataintegrityRules) {
        Map<DataintegrityRule, RuleDefn<T>> rulesWithDefn = new HashMap<>();

        for (DataintegrityRule rule : dataintegrityRules){
            RuleDefn<T> ruleDefn = null;

            if("java".equals(rule.getRuleType())){
                ruleDefn = getJavaRuleInstance(rule.getRuleCode());
            }
            /*
            else if("groovy".equals(rule.getRuleType())){
            }
            else if("sql".equals(rule.getRuleType())){
            }*/

            if(ruleDefn != null) rulesWithDefn.put(rule, ruleDefn);
        }
        return rulesWithDefn;
    }

    private RuleDefn<T> getJavaRuleInstance(String ruleCode) {
        RuleDefn<T> ruleDefn = null;
        try {
            Class<?> ruleClass = OpenmrsClassLoader.getInstance().loadClass(ruleCode, false);
            Object o = ruleClass.newInstance();
            ruleDefn = (RuleDefn<T>) o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //log
        } catch (Exception e) {
            e.printStackTrace();
            //log
        }
        return ruleDefn;
    }
}
