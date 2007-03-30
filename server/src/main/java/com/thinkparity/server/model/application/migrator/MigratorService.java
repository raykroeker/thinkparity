/*
 * Generated On: Sep 04 06 04:29:31 PM
 */
package com.thinkparity.desdemona.model.migrator;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.ThinkParityException;

/**
 * <b>Title:</b>thinkParity Migrator Model Service</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
public final class MigratorService {

    /** A singleton instance of the <code>MigratorService</code>. */
    private static final MigratorService SINGLETON;

    static {
        SINGLETON = new MigratorService();
    }

    /**
     * Obtain an instance of the <code>MigratorService</code>.
     * 
     * @return The <code>MigratorService</code>.
     */
    public static MigratorService getInstance() {
        return SINGLETON;
    }

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** An online/offline <code>Boolean</code>. */
    private Boolean online;

    /**
     * Create MigratorService.
     * 
     */
    private MigratorService() {
        super();

        this.logger = new Log4JWrapper(getClass());
        this.online = Boolean.FALSE;
    }

    /**
     * Determine if the service is online.
     * 
     * @return True if the service is online.
     */
    public Boolean isOnline() {
        synchronized (this) {
            return isOnlineImpl();
        }
    }

    /**
     * Start the migrator service.
     *
     */
    public void start() {
        synchronized (this) {
            startImpl();
        }
    }

    /**
     * Stop the migrator service.
     * 
     */
    public void stop() {
        synchronized (this) {
            stopImpl();
        }
    }

    /**
     * Determine if the service is online.
     * 
     * @return True if the migrator service is online.
     */
    private Boolean isOnlineImpl() {
        return online;
    }

    /**
     * Start the migrator service.
     *
     */
    private void startImpl() {
        logger.logInfo("Starting migrator service.");
        try {
            online = Boolean.TRUE;
            logger.logInfo("Migrator service is online.");
        } catch (final Throwable t) {
            throw new ThinkParityException("Failed to start migrator service.", t);
        }
    }

    /**
     * Stop the migrator service.
     * 
     */
    private void stopImpl() {
        logger.logInfo("Stopping migrator service .");
        try {
            logger.logInfo("Migrator service is offline.");
        } catch (final Throwable t) {
            throw new ThinkParityException("Failed to stop migrator service.", t);
        }
    }
}
