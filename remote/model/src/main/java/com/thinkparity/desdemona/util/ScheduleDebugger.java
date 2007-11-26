/*
 * Created On:  23-Nov-07 4:08:45 PM
 */
package com.thinkparity.desdemona.util;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <b>Title:</b>thinkParity Desdemona Schedule Debugger<br>
 * <b>Description:</b>A schedule debugger that prints the details of the
 * job/time of execution.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ScheduleDebugger implements Job {

    /** A log4j wrapper. */
    private static final Log4JWrapper logger;

    static {
        logger = new Log4JWrapper(ScheduleDebugger.class);
    }

    /**
     * Create ScheduleDebugger.
     *
     */
    public ScheduleDebugger() {
        super();
    }

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     *
     */
    @Override
    public void execute(final JobExecutionContext context)
            throws JobExecutionException {
        final JobDetail jobDetail = context.getJobDetail();
        logger.logInfo("Executing job {0}/{1}.", jobDetail.getGroup(),
                jobDetail.getName());
    }
}
