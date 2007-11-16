/*
 * Created On:  9-Nov-07 3:30:29 PM
 */
package com.thinkparity.ophelia.model.session.daemon;

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

/**
 * <b>Title:</b>thinkParity Ophelia Model Session Reaper<br>
 * <b>Description:</b>A daemon job that checks the date/time of the last event
 * processed by the queue and if it exceeds a threshold; terminates the session.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SessionReaper implements Runnable,
        ReconfigureListener<Configuration> {

    /** The configuration name for the interrupt threshold. */
    private static final String CFG_NAME_INTERRUPT_THRESHOLD;

    /** The configuration name for the timeout. */
    private static final String CFG_NAME_TIMEOUT;

    /** The configuration name for the timeout margin. */
    private static final String CFG_NAME_TIMEOUT_MARGIN;

    /** A date format. */
    private static final SimpleDateFormat DATE_FORMAT;

    /** An interrupt count threshold. */
    private static final Integer DEFAULT_INTERRUPT_THRESHOLD;

    /** A default event timeout. */
    private static final Long DEFAULT_TIMEOUT;

    /** A default event timeout margin. */
    private static final Long DEFAULT_TIMEOUT_MARGIN;

    /** A duration format. */
    private static final TimeFormat DURATION_FORMAT;

    /** A format date. */
    private static final Date FORMAT_DATE;

    static {
        CFG_NAME_TIMEOUT = "com.thinkparity.session.reaper.timeout";
        CFG_NAME_TIMEOUT_MARGIN = "com.thinkparity.session.reaper.timeoutmargin";
        CFG_NAME_INTERRUPT_THRESHOLD = "com.thinkparity.session.reaper.interruptthreshold";
        DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
        DEFAULT_INTERRUPT_THRESHOLD = 3;
        DEFAULT_TIMEOUT = 14400000L;        /* TIMEOUT - SessionReaper - 4H   */
        DEFAULT_TIMEOUT_MARGIN = 288000L;   /* TIMEOUT - SessionReaper - 2.8m */
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

    /** A sleep interruption count. */
    private int interruptCount;

    /** An interrupt threshold. */
    private Integer interruptThreshold;

    /** The last time the reaper executed. */
    private long latestExecutionTime;

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** An internal queue model. */
    private final InternalQueueModel queueModel;

    /** An internal session model. */
    private final InternalSessionModel sessionModel;

    /** An timeout in milliseconds. */
    private Long timeout;

    /** An timeout margin in milliseconds. */
    private Long timeoutMargin;

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

        setInterruptThreshold(sessionModel.getConfiguration());
        setTimeout(sessionModel.getConfiguration());
        setTimeoutMargin(sessionModel.getConfiguration());
    }

    /**
     * @see com.thinkparity.ophelia.model.util.configuration.ReconfigureListener#reconfigure(com.thinkparity.ophelia.model.util.configuration.ReconfigureEvent)
     *
     */
    @Override
    public void reconfigure(final ReconfigureEvent<Configuration> event) {
        if (event.isReconfigured(CFG_NAME_INTERRUPT_THRESHOLD)) {
            logger.logInfo("Reconfiguring session reaper's interrupt threshold.");
            setInterruptThreshold(event.getCurrent());
        } else if (event.isReconfigured(CFG_NAME_TIMEOUT)) {
            logger.logInfo("Reconfiguring session reaper's timeout.");
            setTimeout(event.getCurrent());
        } else if (event.isReconfigured(CFG_NAME_TIMEOUT_MARGIN)) {
            logger.logInfo("Reconfiguring session reaper's timeout margin.");
            setTimeoutMargin(event.getCurrent());
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.util.daemon.DaemonJob#invoke()
     *
     */
    @Override
    public void run() {
        interruptCount = 0;
        while (true) {
            final long now = System.currentTimeMillis();
            logger.logVariable("sessionModel.isOnline()", sessionModel.isOnline());
            final long latestProcessTime = queueModel.getLatestProcessTimeMillis();
            logger.logVariable("latestProcessTime", format(latestProcessTime));
            logger.logVariable("latestExecutionTime", format(latestExecutionTime));
            logger.logVariable("timeout", formatDuration(timeout));
            if (isQueueTimeout(now, latestProcessTime) && isExecutionTimeout(now) && sessionModel.isOnline()) {
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
            try {
                final long sleepMillis = getSleepMillis(now, latestProcessTime);
                if (0 > sleepMillis) {
                    /* we break out of the reaper because we have been "offline"
                     * longer than we'd like */
                    logger.logInfo("Terminating session reaper.");
                    break;
                } else {
                    logger.logInfo("Session reaper sleeping.");
                    Thread.sleep(getSleepMillis(now, latestProcessTime));
                }
            } catch (final InterruptedException ix) {
                interruptCount++;
                logger.logError(ix, "Session reaper interrupted:  {0}/{1}",
                        interruptCount, DEFAULT_INTERRUPT_THRESHOLD);
                if (interruptCount + 1 > interruptThreshold ) {
                    interruptCount = 0;
                    /* we break out of the reaper because we have encountered
                     * too many interrupts (unlikely) */
                    logger.logInfo("Terminating session reaper.");
                    break;
                }
            }
        }
    }

    /**
     * Obtain the sleep duration. Note that the sleep duration can be negative
     * if the session remains offline for a period longer than the last
     * execution/process time plus the timeout.
     * 
     * @param timeMillis
     *            A <code>long</code>.
     * @param latestProcessTimeMillis
     *            A <code>long</code>.
     * @return A <code>long</code>.
     */
    private long getSleepMillis(final long timeMillis,
            final long latestProcessTimeMillis) {
        final long latest = Math.max(latestExecutionTime, latestProcessTimeMillis);
        logger.logVariable("latest", format(latest));
        final long sleep = latest + timeout - timeMillis;
        logger.logVariable("sleep", formatDuration(sleep));
        return sleep;
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
        final long executionDuration = timeMillis - latestExecutionTime;
        logger.logVariable("executionDuration", formatDuration(executionDuration));
        if (0 > executionDuration) {
            return false;
        } else {
            return (timeout - timeoutMargin) < executionDuration;
        }
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
    private boolean isQueueTimeout(final long timeMillis, final long latestProcessTimeMillis) {
        final long queueDuration = timeMillis - latestProcessTimeMillis;
        logger.logVariable("queueDuration", formatDuration(queueDuration));
        if (0 > queueDuration) {
            return false;
        } else {
            return (timeout - timeoutMargin) < queueDuration;
        }
    }

    /**
     * Set the interrupt threshold from the session configuration.
     * 
     * @param configuration
     *            A <code>Properties</code>.
     */
    private void setInterruptThreshold(final Properties configuration) {
        try {
            interruptThreshold = Integer.valueOf(configuration.getProperty(
                            CFG_NAME_INTERRUPT_THRESHOLD,
                            DEFAULT_INTERRUPT_THRESHOLD.toString()));
        } catch (final NumberFormatException nfx) {
            interruptThreshold = DEFAULT_INTERRUPT_THRESHOLD;
        }
    }

    /**
     * Set the timeout from the session configuration.
     * 
     * @param configuration
     *            A <code>Properties</code>.
     */
    private void setTimeout(final Properties configuration) {
        try {
            timeout = Long.valueOf(configuration.getProperty(
                            CFG_NAME_TIMEOUT,
                            DEFAULT_TIMEOUT.toString()));
        } catch (final NumberFormatException nfx) {
            timeout = DEFAULT_TIMEOUT;
        }
    }

    /**
     * Set the timeout margin from the session configuration.
     * 
     * @param configuration
     *            A <code>Properties</code>.
     */
    private void setTimeoutMargin(final Properties configuration) {
        try {
            timeoutMargin = Long.valueOf(configuration.getProperty(
                            CFG_NAME_TIMEOUT_MARGIN,
                            DEFAULT_TIMEOUT_MARGIN.toString()));
        } catch (final NumberFormatException nfx) {
            timeoutMargin = DEFAULT_TIMEOUT_MARGIN;
        }
    }
}
