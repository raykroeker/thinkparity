/*
 * Created On:  4-Sep-07 4:03:42 PM
 */
package com.thinkparity.desdemona.model.profile.payment;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.ModelFactory;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PaymentInvoiceProcessor implements Runnable {

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
    private ModelFactory modelFactory;

    /** A run indicator. */
    private boolean run;

    /** The sleep duration. */
    private Long sleep;

    /**
     * Create PaymentInvoiceProcessor.
     *
     */
    PaymentInvoiceProcessor() {
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
        modelFactory = ModelFactory.getInstance(loader);
        while (run) {
            logger.logTraceId();
            synchronized (this) {
                try {
                    logger.logTraceId();
                    logger.logInfo("Going to sleep.");
                    logger.logVariable("sleep", sleep);
                    wait(sleep);
                } catch (final InterruptedException ix) {
                    logger.logInfo("Waking up.", ix.getMessage());
                }
            }
            if (run) {
                logger.logTraceId();
                logger.logInfo("Processing payment invoice queue.");
                try {
                    modelFactory.getProfileModel().processInvoiceQueue();
                    errCount = 0;
                } catch (final Throwable t) {
                    logger.logError(t, "Could not process payment invoice queue.");
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
     * Obtain the sleep.
     *
     * @return A <code>Long</code>.
     */
    Long getSleep() {
        return sleep;
    }

    /**
     * Set the sleep.
     *
     * @param sleep
     *		A <code>Long</code>.
     */
    void setSleep(final Long sleep) {
        this.sleep = sleep;
    }

    /**
     * Stop the invoice processor.
     * 
     */
    void stop() {
        run = false;
    }
}
