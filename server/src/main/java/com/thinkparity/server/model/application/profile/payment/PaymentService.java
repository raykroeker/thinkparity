/*
 * Created On:  4-Sep-07 12:45:41 PM
 */
package com.thinkparity.desdemona.model.profile.payment;

import java.util.concurrent.Executors;


/**
 * <b>Title:</b>thinkParity Desdemona Model Payment Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentService {

    /** A singleton instance. */
    private static final PaymentService SINGLETON;

    static {
        SINGLETON = new PaymentService();
    }

    /**
     * Obtain an instance of the payment service.
     * 
     * @return A <code>PaymentService</code>.
     */
    public static PaymentService getInstance() {
        return SINGLETON;
    }

    /** An invoice processor. */
    private PaymentInvoiceProcessor invoiceProcessor;

    /** A queue processor. */
    private PaymentQueueProcessor queueProcessor;

    /**
     * Create PaymentService.
     *
     */
    private PaymentService() {
        super();
    }

    /**
     * Start the payment service.
     * 
     */
    public void start() throws PaymentException {
        invoiceProcessor = new PaymentInvoiceProcessor();
        invoiceProcessor.setSleep(23L * 60L * 60L * 10000L); // TIMEOUT 23H
        try {
            Thread.sleep(20L * 1000L);
        } catch (final InterruptedException ix) {
            throw new PaymentException(ix);
        }
        startInvoiceProcessor();

        queueProcessor = new PaymentQueueProcessor();
        queueProcessor.setSleep(23L * 60L * 1000L);  // TIMEOUT 23H
        startQueueProcessor();
    }

    /**
     * Stop the payment service.
     * 
     */
    public void stop() {
        invoiceProcessor.stop();
        invoiceProcessor = null;

        queueProcessor.stop();
        queueProcessor = null;
    }

    /**
     * Wake the service.
     * 
     */
    public void wake() {
        synchronized (invoiceProcessor) {
            invoiceProcessor.notify();
        }
        synchronized (queueProcessor) {
            queueProcessor.notify();
        }
    }

    /**
     * Start a queue processor thread.
     * 
     */
    private void startInvoiceProcessor() {
        startThread(invoiceProcessor, "TPS-DesdemonaModel-InvoiceProcessor");
    }

    /**
     * Start a queue processor thread.
     * 
     */
    private void startQueueProcessor() {
        startThread(queueProcessor, "TPS-DesdemonaModel-PaymentProcessor");
    }

    /**
     * Create a new daemon thread.
     * 
     * @param runnable
     *            A <code>Runnable</code>.
     * @param name
     *            A <code>String</code>.
     * @return A <code>Thread</code>.
     */
    private void startThread(final Runnable runnable, final String name) {
        final Thread thread = Executors.defaultThreadFactory().newThread(runnable);
        thread.setDaemon(true);
        thread.setName(name);
        thread.start();
    }
}
