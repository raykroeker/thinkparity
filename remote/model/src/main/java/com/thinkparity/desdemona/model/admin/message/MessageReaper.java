/*
 * Created On:  23-Nov-07 2:16:47 PM
 */
package com.thinkparity.desdemona.model.admin.message;

import java.util.Calendar;
import java.util.Properties;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.InvalidTokenException;

import com.thinkparity.desdemona.service.application.ApplicationService;

import com.thinkparity.desdemona.util.DateTimeProvider;
import com.thinkparity.desdemona.util.DesdemonaProperties;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <b>Title:</b>thinkParity Desdemona Model Admin Message Reaper<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MessageReaper implements Job {

    /** A default maximum age. */
    private static final long DEFAULT_MAXIMUM_AGE;

    /** A job log4j wrapper. */
    private static final Log4JWrapper logger;

    /** The maximum age property name. */
    private static final String PROPERTY_NAME_MAXIMUM_AGE;

    static {
        logger = new Log4JWrapper(MessageReaper.class);

        DEFAULT_MAXIMUM_AGE = 604800000L;   // 1 week
        PROPERTY_NAME_MAXIMUM_AGE = "thinkparity.ops.messagebus.messagereaper.maximumage";
    }

    /** The maximum message age. */
    private long maximumAge;

    /**
     * Create MessageReaper.
     *
     */
    public MessageReaper() {
        super();

        setMaximumAge(DesdemonaProperties.getInstance());
    }

    /**
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     *
     */
    @Override
    public void execute(final JobExecutionContext context)
            throws JobExecutionException {
        logger.logInfo("Executing message reaper.");
        if (MessageBus.isEnabled()) {
            logger.logDebug("Message bus is enabled.");
            try {
                newModel().expire(MessageBus.getToken(), newExpiryTimestamp());
            } catch (final InvalidTokenException itx) {
                final JobExecutionException jex = new JobExecutionException(itx, false);
                jex.setUnscheduleAllTriggers(true);
                throw jex;
            }
        } else {
            logger.logDebug("Message bus is enabled.");
        }
    }

    /**
     * Obtain the maximum age in milliseconds.
     * 
     * @return A <code>long</code>.
     */
    private long getMaximumAge() {
        return maximumAge;
    }

    /**
     * Instantiate an expiry timestamp. Based upon the configuration for the
     * maximum age for a message.
     * 
     * @return A <code>Calendar</code>.
     */
    private Calendar newExpiryTimestamp() {
        final Calendar currentTimestamp = DateTimeProvider.getCurrentDateTime();
        final Calendar expiryTimestamp = (Calendar) currentTimestamp.clone();
        expiryTimestamp.setTimeInMillis(currentTimestamp.getTimeInMillis() - getMaximumAge());
        return expiryTimestamp;
    }

    /**
     * Instantiate a model.
     * 
     * @return An <code>InternalMessageModel</code>.
     */
    private InternalMessageModel newModel() {
        return ApplicationService.getInstance().getInternalAdminModelFactory().newMessageModel();
    }

    /**
     * Set the maximum age.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     */
    private void setMaximumAge(final Properties properties) {
        try {
            maximumAge = Long.parseLong(properties.getProperty(
                    PROPERTY_NAME_MAXIMUM_AGE,
                    String.valueOf(DEFAULT_MAXIMUM_AGE)));
        } catch (final NumberFormatException nfx) {
            maximumAge = DEFAULT_MAXIMUM_AGE;
        }
    }
}
