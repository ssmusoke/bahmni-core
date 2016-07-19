package org.bahmni.module.dataintegrity.factory;

import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.rule.RuleDefn;

import java.util.List;

public interface RuleDefnsFactory<T> {
     List<RuleDefn<T>> getRuleDefns(List<DataIntegrityRule> dataIntegrityRules);
}
