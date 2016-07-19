package org.bahmni.module.dataintegrity.rule.factory;

import org.bahmni.module.dataintegrity.db.DataIntegrityRule;
import org.bahmni.module.dataintegrity.rule.RuleDefn;

public interface RuleDefnLoader<T> {
    RuleDefn<T> getRuleDefn(DataIntegrityRule rule);
}
