/*
 * Created On:  9-Nov-07 4:14:00 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.util.Timer;
import java.util.TimerTask;

import com.thinkparity.ophelia.model.util.daemon.DaemonJob;
import com.thinkparity.ophelia.model.util.daemon.DaemonSchedule;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Scheduler Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class SchedulerImpl {

    /** A timer. */
    private Timer timer;

    /**
     * Create SchedulerImpl.
     *
     */
    SchedulerImpl(final Workspace workspace) {
        super();
    }

    /**
     * Schedule a daemon job.
     * 
     * @param job
     *            A <code>DaemonJob</code>.
     * @param schedule
     *            A <code>DaemonSchedule</code>.
     */
    void schedule(final DaemonJob job, final DaemonSchedule schedule) {
        if (schedule.isRecurring()) {
            timer.schedule(new TimerTask() {
                /**
                 * @see java.util.TimerTask#run()
                 * 
                 */
                @Override
                public void run() {
                    job.execute();
                }
            }, schedule.getFirstExecution(),
                    schedule.getRecurringExecutionPeriod());
        } else {
            timer.schedule(new TimerTask() {
                /**
                 * @see java.util.TimerTask#run()
                 *
                 */
                @Override
                public void run() {
                    job.execute();
                }
            }, schedule.getFirstExecution());
        }
    }

    /**
     * Start the scheduler.
     * 
     */
    void start() {
        timer = new Timer();
    }

    /**
     * Stop the scheduler.
     * 
     */
    void stop() {
        try {
            timer.cancel();
        } finally {
            timer = null;
        }
    }
}
