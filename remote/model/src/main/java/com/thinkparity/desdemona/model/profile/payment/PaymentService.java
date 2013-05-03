/*
 * Created On:  4-Sep-07 12:45:41 PM
 */
package com.thinkparity.desdemona.model.profile.payment;

import java.util.Properties;
import java.util.concurrent.Executors;


/**
 * <b>Title:</b>thinkParity Desdemona Model Payment Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentService {

    /** A default sleep duration. */
    private static final Long DEFAULT_SLEEP;

    /** A singleton instance. */
    private static final PaymentService SINGLETON;

    static {
        DEFAULT_SLEEP = 23L * 60L * 60L * 1000L; // TIMEOUT 23H
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

    /** A properties. */
    private Properties properties;

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
    public void start(final Properties properties) throws PaymentException {
        this.properties = properties;

        invoiceProcessor = new PaymentInvoiceProcessor();
        invoiceProcessor.setSleep(getSleep("thinkparity.payment.invoicesleep"));
        startInvoiceProcessor();

        queueProcessor = new PaymentQueueProcessor();
        queueProcessor.setSleep(getSleep("thinkparity.payment.plansleep"));
        startQueueProcessor();

        wakeInvoiceProcessor();
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
     * Wake the invoice processor.
     * 
     */
    public void wakeInvoiceProcessor() {
        synchronized (invoiceProcessor) {
            invoiceProcessor.setSleep(getSleep("thinkparity.payment.invoicesleep"));
            invoiceProcessor.notify();
        }
    }

    /**
     * Wake the payment processor.
     * 
     */
    public void wakePaymentProcessor() {
        synchronized (queueProcessor) {
            queueProcessor.setSleep(getSleep("thinkparity.payment.plansleep"));
            queueProcessor.notify();
        }
    }

    /**
     * Obtain the sleep property.
     * 
     * @param key
     *            A <code>String</code>.
     * @return A <code>Long</code>.
     */
    private Long getSleep(final String key) {
        try {
            return Long.valueOf(properties.getProperty(key,
                    String.valueOf(DEFAULT_SLEEP)));
        } catch (final NumberFormatException nfx) {
            return DEFAULT_SLEEP;
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
