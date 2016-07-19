package org.bahmni.module.dataintegrity.scheduler;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.dataintegrity.rule.RuleEngine;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementation of a task that runs all the DI rules available.
 *
 */
public class DataIntegrityTask extends AbstractTask {

    private static Log log = LogFactory.getLog(DataIntegrityTask.class);
    private RuleEngine ruleEngine;

    @Autowired
    public DataIntegrityTask(RuleEngine ruleEngine) {
        this.ruleEngine = ruleEngine;
        log.debug("DataIntegrityTask created at " + new Date());
    }

    public void execute() {
        ruleEngine.fireRules();
        log.debug("executing hello world task");
        super.startExecuting();
    }

    public void shutdown() {
        log.debug("shutting down hello world task");
        this.stopExecuting();
    }
}
