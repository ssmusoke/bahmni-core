package org.bahmni.module.dataintegrity.rule.factory;

import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.rule.RuleDefn;

import java.util.List;
import java.util.Map;

public interface RuleDefnsFactory<T> {
     Map<DataIntegrityRule, RuleDefn<T>> getRuleDefns(List<DataIntegrityRule> dataIntegrityRules);
}
