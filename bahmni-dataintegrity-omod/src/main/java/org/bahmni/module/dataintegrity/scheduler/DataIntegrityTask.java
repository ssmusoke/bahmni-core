package org.bahmni.module.dataintegrity.scheduler;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bahmni.module.dataintegrity.rule.DataintegrityEvaluationService;
import org.bahmni.module.dataintegrity.rule.impl.DataintegrityEvaluationServiceImpl;
import org.openmrs.api.context.Context;
import org.openmrs.scheduler.tasks.AbstractTask;

/**
 * Implementation of a task that runs all the DI rules available.
 *
 */
public class DataIntegrityTask extends AbstractTask {

    private static Log log = LogFactory.getLog(DataIntegrityTask.class);

    public DataIntegrityTask() {
        log.debug("DataIntegrityTask created at " + new Date());
    }

    public void execute() {
        DataintegrityEvaluationService evaluationService = Context.getService(DataintegrityEvaluationService.class);
        evaluationService.fireRules();
        log.debug("executing hello world task");
        super.startExecuting();
    }

    public void shutdown() {
        log.debug("shutting down hello world task");
        this.stopExecuting();
    }
}
