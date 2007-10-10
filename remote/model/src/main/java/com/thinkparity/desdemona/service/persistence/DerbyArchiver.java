/*
 * Created On:  9-Oct-07 1:30:41 PM
 */
package com.thinkparity.desdemona.service.persistence;

import java.util.TimerTask;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.admin.AdminModelFactory;

/**
 * <b>Title:</b>thinkParity Desdmona Persistence Service Archive Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class DerbyArchiver extends TimerTask {

    /** An error count threshold. */
    private static final int ERR_COUNT_THRESHOLD;

    /** An error retry timeout. */
    private static final int ERR_RETRY_TIMEOUT;

    /** An error counter. */
    private static int errCount;

    static {
        ERR_COUNT_THRESHOLD = -1;
        ERR_RETRY_TIMEOUT = 3 * 1000;
        errCount = 0;
    }

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** A model factory. */
    private AdminModelFactory modelFactory;

    /** A run indicator. */
    private boolean run;

    /**
     * Create ArchiveDelegate.
     *
     */
    DerbyArchiver() {
        super();
        this.logger = new Log4JWrapper();
        this.run = true;
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    @Override
    public void run() {
        logger.logTraceId();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        modelFactory = AdminModelFactory.getInstance(User.THINKPARITY, loader);
        while (run) {
            logger.logTraceId();
            if (run) {
                logger.logTraceId();
                logger.logInfo("Issuing derby archive.");
                try {
                    modelFactory.newDerbyModel().archive();
                    errCount = 0;
                } catch (final Throwable t) {
                    logger.logError(t, "Could not issue derby archive.");
                    errCount++;
                    if (errCount > ERR_COUNT_THRESHOLD) {
                        run = false;
                        logger.logTraceId();
                        logger.logFatal("Stopping.");
                    } else {
                        try {
                            Thread.sleep(ERR_RETRY_TIMEOUT);
                        } catch (final InterruptedException ix) {
                            logger.logWarning("Could not wait for err timeout.",
                                    ix.getMessage());
                        }
                    }
                }
            } else {
                logger.logTraceId();
                logger.logInfo("Stopping.");
            }
        }
    }

    /**
     * Stop the archive delegate.
     * 
     */
    void stop() {
        run = false;
    }
}
