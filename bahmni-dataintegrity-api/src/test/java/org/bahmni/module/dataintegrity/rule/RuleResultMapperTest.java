package org.bahmni.module.dataintegrity.rule;

import org.bahmni.module.dataintegrity.db.DataintegrityResult;
import org.bahmni.module.dataintegrity.db.DataintegrityRule;
import org.bahmni.module.dataintegrity.rule.impl.PatientProgramRuleDefn;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.PatientProgram;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RuleResultMapperTest {

    RuleResultMapper resultMapper;
    List<RuleResult> inputresult;
    Map<DataintegrityRule, RuleDefn> rulesMap;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        inputresult = new ArrayList<>();
        resultMapper = new RuleResultMapper();
        rulesMap = new HashMap<>();
    }

    @Test
    public void shouldReturnDIResultForOneRule() throws Exception {
        List diResults = null;
        DataintegrityRule rule = new DataintegrityRule();
        rule.setId(Integer.valueOf("1001"));
        rule.setRuleName("TestRule");

        rulesMap.put(rule, new RuleDefn() {
            @Override
            public List<RuleResult> evaluate() {
                return null;
            }
        });
        resultMapper = new RuleResultMapper<>();
        inputresult.add( new RuleResult());
        for(Entry<DataintegrityRule, RuleDefn> ruleEntry : rulesMap.entrySet())
            diResults = resultMapper.getDataintegrityResults(ruleEntry, inputresult);

        Assert.notNull(diResults);
        Assert.isTrue(diResults.size() == 1);
    }

    @Test
    @PrepareForTest(PatientProgramRuleDefn.class)
    public void shouldReturnDIResultForPatientProgramType() throws Exception {
        List<DataintegrityResult> diResults = null;
        PatientProgramRuleDefn patientProgramRuleDefn = mock(PatientProgramRuleDefn.class);
        when(patientProgramRuleDefn.evaluate()).thenReturn(null);

        DataintegrityRule rule = new DataintegrityRule();
        rule.setId(Integer.valueOf("1001"));
        rule.setRuleName("TestRule");
        rulesMap.put(rule, new RuleDefn() {
            @Override
            public List<RuleResult> evaluate() {
                return null;
            }
        });
        rulesMap.put(rule, patientProgramRuleDefn);
        resultMapper = new RuleResultMapper<PatientProgram>();
        inputresult.add( new RuleResult());
        for(Entry<DataintegrityRule, RuleDefn> ruleEntry : rulesMap.entrySet())
            diResults = resultMapper.getDataintegrityResults(ruleEntry, inputresult);

        Assert.isTrue(diResults.size() == 1);
        Assert.notNull(diResults.get(0).getPatientProgram());
    }

    @Test
    public void shouldNotThrowExceptionForEmptyRulesList() throws Exception {

        List diResults = null;
        for(Entry<DataintegrityRule, RuleDefn> ruleEntry : rulesMap.entrySet())
            diResults = resultMapper.getDataintegrityResults(ruleEntry, inputresult);

        Assert.isNull(diResults);
    }

    @Test
    public void shouldReturnEmptyListForEmptyRuleMapValue() throws Exception {

        DataintegrityRule rule = new DataintegrityRule();
        rule.setId(Integer.valueOf("1001"));
        rule.setRuleName("TestRule");

        rulesMap.put(rule, null);

        List diResults = null;
        for(Entry<DataintegrityRule, RuleDefn> ruleEntry : rulesMap.entrySet())
            diResults = resultMapper.getDataintegrityResults(ruleEntry, inputresult);

        Assert.notNull(diResults);
        Assert.isTrue(diResults.isEmpty());
    }

}