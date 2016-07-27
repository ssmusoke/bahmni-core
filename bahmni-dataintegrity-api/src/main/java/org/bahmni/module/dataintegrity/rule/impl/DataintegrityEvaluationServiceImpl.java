package org.bahmni.module.dataintegrity.rule.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.dataintegrity.rule.DataintegrityEvaluationService;
import org.bahmni.module.dataintegrity.rule.RuleDefn;
import org.bahmni.module.dataintegrity.rule.RuleDefnLoader;
import org.bahmni.module.dataintegrity.rule.RuleResult;
import org.bahmni.module.dataintegrity.db.DataintegrityDao;
import org.bahmni.module.dataintegrity.db.DataintegrityResult;
import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.bahmni.module.dataintegrity.rule.RuleResultMapper;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;

@Component("DataintegrityEvaluationService")
public class DataintegrityEvaluationServiceImpl<T> implements DataintegrityEvaluationService<T> {
    private static Log log = LogFactory.getLog(DataintegrityEvaluationServiceImpl.class);
    private DataintegrityDao dataintegrityDao;

    @Override
    public void fireRules(){
        RuleResultMapper resultMapper = new RuleResultMapper();
        List<DataintegrityResult> results = new ArrayList<>();

        dataintegrityDao.clearAllResults();

        Map<DataintegrityRule, RuleDefn<T>> rulesWithDefns = loadRuleDefns();

        for (Entry<DataintegrityRule, RuleDefn<T>> ruleWithDefn : rulesWithDefns.entrySet()) {
            try {
                List<RuleResult<T>> ruleResults = ruleWithDefn.getValue().evaluate();
                results.addAll(resultMapper.getDataintegrityResults(ruleWithDefn, ruleResults));
            }catch (Exception e){
                log.error(MessageFormat.format("ERROR executing DataIntegrity Rule : {0} with follwoing Exception - {1}{2}",
                        ruleWithDefn.getKey().getRuleCode(), e.toString(), getStackTrace(e)));
            }
        }
        dataintegrityDao.saveResults(results);

    }
    private Map<DataintegrityRule, RuleDefn<T>> loadRuleDefns() {
        RuleDefnLoader<T> loader = new RuleDefnLoader<>();
        List<DataintegrityRule> diRules = dataintegrityDao.getRules();
        return loader.getRuleDefns(diRules);
    }

    public void setDataintegrityDao(DataintegrityDao dataintegrityDao) {
        this.dataintegrityDao = dataintegrityDao;
    }

    public DataintegrityDao getDataintegrityDao() {
        return dataintegrityDao;
    }
}
