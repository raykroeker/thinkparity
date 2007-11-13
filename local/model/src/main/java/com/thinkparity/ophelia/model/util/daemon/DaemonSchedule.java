/*
 * Created On:  9-Nov-07 4:07:49 PM
 */
package com.thinkparity.ophelia.model.util.daemon;

import java.util.Date;

/**
 * <b>Title:</b>thinkParity Ophelia Model Daemon Schedule<br>
 * <b>Description:</b>A schedule for invoking a daemon.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface DaemonSchedule {

    /**
     * Obtain the date of the first execution.
     * 
     * @return A <code>Date</code>.
     */
    Date getFirstExecution();

    /**
     * Obtain the recurring execution period in milliseconds.
     * 
     * @return A <code>Long</code>.
     */
    Long getRecurringExecutionPeriod();

    /**
     * Determine if the schedule provides for recurrance.
     * 
     * @return True if the execution is to recurr.
     */
    Boolean isRecurring();
}
