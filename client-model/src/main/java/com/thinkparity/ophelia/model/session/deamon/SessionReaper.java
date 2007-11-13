/*
 * Created On:  9-Nov-07 3:30:29 PM
 */
package com.thinkparity.ophelia.model.session.deamon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.thinkparity.codebase.TimeFormat;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Configuration;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.queue.InternalQueueModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.util.configuration.ReconfigureEvent;
import com.thinkparity.ophelia.model.util.configuration.ReconfigureListener;
import com.thinkparity.ophelia.model.util.daemon.DaemonJob;
import com.thinkparity.ophelia.model.util.daemon.DaemonSchedule;

/**
 * <b>Title:</b>thinkParity Ophelia Model Session Reaper<br>
 * <b>Description:</b>A daemon job that checks the date/time of the last event
 * processed by the queue and if it exceeds a threshold; terminates the session.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SessionReaper implements DaemonJob, DaemonSchedule,
        ReconfigureListener<Configuration> {

    /** The configuration name for the event timeout. */
    private static final String CFG_NAME_EVENT_TIMEOUT;

    /** The configuration name for the first execution delay. */
    private static final String CFG_NAME_FIRST_EXECUTION_DELAY;

    /** The configuration name for the recurring execution period. */
    private static final String CFG_NAME_RECURRING_EXECUTION_PERIOD;

    /** A date format. */
    private static final SimpleDateFormat DATE_FORMAT;

    /** A default event timeout. */
    private static final Long DEFAULT_EVENT_TIMEOUT;

    /** The default first execution delay. */
    private static final Long DEFAULT_FIRST_EXECUTION_DELAY;

    /** The default recurring execution period. */
    private static final Long DEFAULT_RECURRING_EXECUTION_PERIOD;

    /** A duration format. */
    private static final TimeFormat DURATION_FORMAT;

    /** A format date. */
    private static final Date FORMAT_DATE;

    static {
        CFG_NAME_EVENT_TIMEOUT = "com.thinkparity.session.reaper.timeout";
        CFG_NAME_FIRST_EXECUTION_DELAY = "com.thinkparity.session.reaper.firstexecutiondelay";
        CFG_NAME_RECURRING_EXECUTION_PERIOD = "com.thinkparity.session.reaper.recurringexecutionperiod";
        DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
        DEFAULT_EVENT_TIMEOUT = 14400000L; /* TIMEOUT - SessionReaper - 4H */
        DEFAULT_FIRST_EXECUTION_DELAY = 3600000L; /* DELAY - SessionReaper - 1H */
        DEFAULT_RECURRING_EXECUTION_PERIOD = 3600000L; /* PERIOD - SessionReaper - 1H */
        DURATION_FORMAT = new TimeFormat();
        FORMAT_DATE = new Date();
    }

    /**
     * Format the time in milliseconds.
     * 
     * @param timeMillis
     *            A <code>long</code>.
     * @return A <code>String</code>.
     */
    private static String format(final long timeMillis) {
        FORMAT_DATE.setTime(timeMillis);
        return DATE_FORMAT.format(FORMAT_DATE);
    }

    /**
     * Format the duration in milliseconds.
     * 
     * @param durationMillis
     *            A <code>long</code>.
     * @return A <code>String</code>.
     */
    private static String formatDuration(final long durationMillis) {
        return DURATION_FORMAT.format(durationMillis);
    }

    /** An event timeout in milliseconds. */
    private Long eventTimeout;

    /** The first execution date. */
    private Date firstExecution;

    /** The last time the reaper executed. */
    private long latestExecutionTime;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** An internal queue model. */
    private final InternalQueueModel queueModel;

    /** The recurring execution period. */
    private Long recurringExecutionPeriod;

    /** An internal session model. */
    private final InternalSessionModel sessionModel;

    /**
     * Create SessionReaper.
     * 
     * @param modelFactory
     *            An <code>InternalModelFactory</code>.
     */
    public SessionReaper(final InternalModelFactory modelFactory) {
        super();
        this.latestExecutionTime = System.currentTimeMillis();
        this.logger = new Log4JWrapper(getClass());
        this.queueModel = modelFactory.getQueueModel();
        this.sessionModel = modelFactory.getSessionModel();

        setEventTimeout(sessionModel.getConfiguration());
        setFirstExecutionDelay(sessionModel.getConfiguration());
        setRecurringExecutionPeriod(sessionModel.getConfiguration());
    }

    /**
     * @see com.thinkparity.ophelia.model.util.daemon.DaemonJob#invoke()
     *
     */
    @Override
    public void execute() {
        final long now = System.currentTimeMillis();
        logger.logVariable("sessionModel.isOnline()", sessionModel.isOnline());
        if (isQueueTimeout(now) && isExecutionTimeout(now) && sessionModel.isOnline()) {
            logger.logInfo("The session has expired and will be reclaimed.");
            try {
                queueModel.stopNotificationClient();
            } finally {
                try {
                    sessionModel.logout();
                } finally {
                    latestExecutionTime = System.currentTimeMillis();
                }
            }
        } else {
            logger.logInfo("The session has not expired.");
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.util.daemon.DaemonSchedule#getFirstExecution()
     *
     */
    @Override
    public Date getFirstExecution() {
        return firstExecution;
    }

    /**
     * @see com.thinkparity.ophelia.model.util.daemon.DaemonSchedule#getRecurringExecutionPeriod()
     *
     */
    @Override
    public Long getRecurringExecutionPeriod() {
        return recurringExecutionPeriod;
    }

    /**
     * @see com.thinkparity.ophelia.model.util.daemon.DaemonSchedule#isRecurring()
     *
     */
    @Override
    public Boolean isRecurring() {
        return Boolean.TRUE;
    }

    /**
     * @see com.thinkparity.ophelia.model.util.configuration.ReconfigureListener#reconfigure(com.thinkparity.ophelia.model.util.configuration.ReconfigureEvent)
     *
     */
    @Override
    public void reconfigure(final ReconfigureEvent<Configuration> event) {
        if (event.isReconfigured(CFG_NAME_EVENT_TIMEOUT)) {
            logger.logInfo("Reconfiguring session reaper's event timeout.");
            setEventTimeout(event.getCurrent());
        } else if (event.isReconfigured(CFG_NAME_FIRST_EXECUTION_DELAY)) {
            logger.logInfo("Reconfiguring session reaper's first execution delay.");
            setFirstExecutionDelay(event.getCurrent());
        } else if (event.isReconfigured(CFG_NAME_RECURRING_EXECUTION_PERIOD)) {
            logger.logInfo("Reconfiguring session reaper's recurring execution period.");
            setRecurringExecutionPeriod(event.getCurrent());
        }
    }

    /**
     * Determine whether or not the last execution was before the time in
     * millis.
     * 
     * @param timeMillis
     *            A <code>long</code>.
     * @return True if the time minus the last execution time is greater than
     *         the timeout.
     */
    private boolean isExecutionTimeout(final long timeMillis) {
        logger.logVariable("latestExecutionTime", format(latestExecutionTime));
        logger.logVariable("duration", formatDuration(timeMillis - latestExecutionTime));
        logger.logVariable("timeout", formatDuration(eventTimeout));
        return eventTimeout < (timeMillis - latestExecutionTime);
    }

    /**
     * Determine whether or not the last read of the queue was before the time
     * in millis.
     * 
     * @param timeMillis
     *            A <code>long</code>.
     * @return True if the time minus the last queue process time is greater
     *         than the timeout.
     */
    private boolean isQueueTimeout(final long timeMillis) {
        final long latestProcessTime = queueModel.getLatestProcessTimeMillis();        
        logger.logVariable("latestProcessTime", format(latestProcessTime));
        logger.logVariable("duration", formatDuration(timeMillis - latestProcessTime));
        logger.logVariable("timeout", formatDuration(eventTimeout));
        return eventTimeout < (timeMillis - latestProcessTime);
    }

    /**
     * Set the event timeout from the session configuration.
     * 
     * @param configuration
     *            A <code>Properties</code>.
     */
    private void setEventTimeout(final Properties configuration) {
        try {
            eventTimeout = Long.valueOf(configuration.getProperty(
                            CFG_NAME_EVENT_TIMEOUT,
                            DEFAULT_EVENT_TIMEOUT.toString()));
        } catch (final NumberFormatException nfx) {
            eventTimeout = DEFAULT_EVENT_TIMEOUT;
        }
    }

    /**
     * Set the first execution date from the session configuration.
     * 
     */
    private void setFirstExecutionDelay(final Properties configuration) {
        firstExecution = new Date();
        Long delay = null;
        try {
            delay = Long.valueOf(configuration.getProperty(
                            CFG_NAME_FIRST_EXECUTION_DELAY,
                            DEFAULT_FIRST_EXECUTION_DELAY.toString()));
        } catch (final NumberFormatException nfx) {
            delay = DEFAULT_FIRST_EXECUTION_DELAY;
        }
        firstExecution.setTime(firstExecution.getTime() + delay.longValue());
    }

    /**
     * Set the recurring execution period from the session configuration.
     * 
     * @param configuration
     *            A <code>Properties</code>.
     */
    private void setRecurringExecutionPeriod(final Properties configuration) {
        try {
            recurringExecutionPeriod = Long.valueOf(configuration.getProperty(
                            CFG_NAME_RECURRING_EXECUTION_PERIOD,
                            DEFAULT_RECURRING_EXECUTION_PERIOD.toString()));
        } catch (final NumberFormatException nfx) {
            recurringExecutionPeriod = DEFAULT_RECURRING_EXECUTION_PERIOD;
        }
    }
}
