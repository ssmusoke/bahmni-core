package org.bahmni.module.dataintegrity.rule.factory.impl;

import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.rule.factory.RuleDefnLoader;
import org.bahmni.module.dataintegrity.rule.RuleDefn;

public class JavaRuleDefnLoader<T> implements RuleDefnLoader {

    @Override
    public RuleDefn<T> getRuleDefn(DataIntegrityRule rule) {
        try {
            return (RuleDefn<T>) Class.forName(rule.getRuleCode()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
