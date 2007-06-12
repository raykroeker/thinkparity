/*
 * Created On:  26-May-07 10:37:41 AM
 */
package com.thinkparity.codebase.net.online;

import java.util.Observable;

import com.thinkparity.codebase.PropertiesUtil;
import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity CommonCodebase Net Online Validation Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ValidationService extends Observable implements Runnable {

    /** The validation service <code>Config</code>. */
    private static final Config CONFIG;

    static {
        CONFIG = ConfigFactory.newInstance("net/online/validation-service.properties");
    }

    /**
     * Create an instance of an online validation service.
     * 
     * @return An instance of <code>ValidationService</code>.
     */
    public static ValidationService createInstance(final String provider) {
        PropertiesUtil.verify(CONFIG, provider + ".provider-class");
        PropertiesUtil.verify(CONFIG, provider + ".provider-config");
        return new ValidationService(CONFIG.getProperty(provider + ".provider-class"),
                CONFIG.getProperty(provider + ".provider-config"));
    }

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** The result of the latest online validation. */
    private Boolean online;

    /** An internal run indicator. */
    private Boolean running;

    /** The number of milliseconds to sleep. */
    private final long sleep;

    /** An implementation of a <code>Validator</code>. */
    private final Validator validator;

    /**
     * Create ValidationService.
     * 
     * @param validatorClass
     *            The validator provider class name <code>String</code>.
     * @param validatorConfig
     *            The validator provider configuration path <code>String</code>.
     */
    private ValidationService(final String validatorClass,
            final String validatorConfig) {
        super();
        try {
            final Class<?> type = Class.forName(validatorClass);
            this.validator = (Validator) type.newInstance();
            this.validator.initialize(ConfigFactory.newInstance(validatorConfig));
        } catch (final ClassNotFoundException cnfx) {
            throw new RuntimeException(cnfx);
        } catch (final InstantiationException ix) {
            throw new RuntimeException(ix);
        } catch (final IllegalAccessException iax) {
            throw new RuntimeException(iax);
        }
        this.logger = new Log4JWrapper("com.thinkparity.codebase.net.online");
        this.running = Boolean.TRUE;
        try {
            this.sleep = Long.parseLong(CONFIG.getProperty("sleep"));
        } catch (final NumberFormatException nfx) {
            throw new IllegalArgumentException(nfx);
        }
    }

    /**
     * Determine the result of the latest online validation.
     * 
     * @return True if we are online.
     */
    public Boolean isOnline() {
        return online;
    }

    /**
     * Determine whether or not the validator is runnning.
     *
     * @return True if the validator is running.
     */
    public Boolean isRunnning() {
        return running;
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    public void run() {
        Boolean wasOnline;
        while (running.booleanValue()) {
            wasOnline = online;
            logger.logVariable("wasOnline", wasOnline);
            validateNow();
            logger.logVariable("online", online);
            if (running.booleanValue()) {
                if (null != wasOnline && online != wasOnline) {
                    setChanged();
                    notifyObservers(online);
                }
                try {
                    Thread.sleep(sleep);
                } catch (final InterruptedException ix) {
                    logger.logWarning(ix, "Service was interrupted.");
                }
            }
        }
    }

    /**
     * Set running.
     * 
     * @param running
     *            A <code>Boolean</code> indicator as to whether or not to
     *            continue running.
     */
    public void setRunnning(final Boolean running) {
        this.running = running;
        synchronized (this) {
            notifyAll();
        }
    }

    /**
     * Perform the validation.
     *
     */
    private void validateNow() {
        online = validator.validate();
    }
}
