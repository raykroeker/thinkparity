/*
 * Created On:  4-Sep-07 9:47:31 AM
 */
package com.thinkparity.payment.moneris;

import com.thinkparity.payment.moneris.delegate.PreAuth;
import com.thinkparity.payment.moneris.delegate.Purchase;
import com.thinkparity.payment.moneris.delegate.RecurringPurchase;

/**
 * <b>Title:</b>thinkParity Moneris Payment Gateway<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentGateway {

    /** A singleton instance. */
    private static final PaymentGateway SINGLETON;

    static {
        SINGLETON = new PaymentGateway();
    }

    /**
     * Obtain an instance of a payment gateway.
     * 
     * @return A <code>PaymentGateway</code>.
     */
    public static PaymentGateway getInstance() {
        return SINGLETON;
    }

    /** The gateway state. */
    private State state;

    /**
     * Create PaymentGateway.
     *
     */
    private PaymentGateway() {
        super();
        open();
    }

    /**
     * Determine whether or not the payment gateway is closed.
     * 
     * @return True if it is closed.
     */
    public Boolean isClosed() {
        return State.CLOSED == state;
    }

    /**
     * Determine whether or not the payment gateway is open.
     * 
     * @return True if it is open.
     */
    public Boolean isOpen() {
        return State.OPEN == state;
    }

    /**
     * Preauthorize.
     * 
     * @param environment
     *            A <code>PaymentEnvironment</code>.
     * @param transaction
     *            A <code>PaymentTransaction</code>.
     * @param amount
     *            A <code>String</code>.
     * @param cardExpiryDate
     *            A <code>String</code>.
     * @param cardNumber
     *            A <code>String</code>.
     * @param customerId
     *            A <code>String</code>.
     */
    void preAuthorize(final PaymentEnvironment environment,
            final PaymentTransaction transaction, final String amount,
            final String cardExpiryDate, final String cardNumber,
            final String customerId) throws Exception {
        final PreAuth delegate = new PreAuth();
        delegate.setAmount(amount);
        delegate.setCardExpiryDate(cardExpiryDate);
        delegate.setCardNumber(cardNumber);
        delegate.setCustomerId(customerId);
        delegate.setEnvironment(environment);
        delegate.setTransaction(transaction);
        delegate.invoke();
    }

    /**
     * Purchase.
     * 
     * @param environment
     *            A <code>PaymentEnvironment</code>.
     * @param transaction
     *            A <code>PaymentTransaction</code>.
     * @param amount
     *            A <code>String</code>.
     * @param cardExpiryDate
     *            A <code>String</code>.
     * @param cardNumber
     *            A <code>String</code>.
     * @param customerId
     *            A <code>String</code>.
     */
    void purchase(final PaymentEnvironment environment,
            final PaymentTransaction transaction, final String amount,
            final String cardExpiryDate, final String cardNumber,
            final String customerId) throws Exception {
        final Purchase delegate = new Purchase();
        delegate.setAmount(amount);
        delegate.setCardExpiryDate(cardExpiryDate);
        delegate.setCardNumber(cardNumber);
        delegate.setCustomerId(customerId);
        delegate.setEnvironment(environment);
        delegate.setTransaction(transaction);
        delegate.invoke();
    }

    /**
     * Schedule a recurring purchase.
     * 
     * @param environment
     *            A <code>PaymentEnvironment</code>.
     * @param transaction
     *            A <code>PaymentTransaction</code>.
     * @param amount
     *            A <code>String</code>.
     * @param cardExpiryDate
     *            A <code>String</code>.
     * @param cardNumber
     *            A <code>String</code>.
     * @param customerId
     *            A <code>String</code>.
     * @param recurringAmount
     *            A <code>String</code>.
     * @param recurringCount
     *            A <code>String</code>.
     * @param recurringImmediately
     *            A <code>String</code>.
     * @param recurringPeriod
     *            A <code>String</code>.
     * @param recurringStartDate
     *            A <code>String</code>.
     * @param recurringUnit
     *            A <code>String</code>.
     */
    void scheduleRecurringPurchase(final PaymentEnvironment environment,
            final PaymentTransaction transaction, final String amount,
            final String cardExpiryDate, final String cardNumber,
            final String customerId, final String recurringAmount,
            final String recurringCount, final String recurringImmediately,
            final String recurringPeriod, final String recurringStartDate,
            final String recurringUnit) throws Exception {
        final RecurringPurchase delegate = new RecurringPurchase();
        delegate.setAmount(amount);
        delegate.setCardExpiryDate(cardExpiryDate);
        delegate.setCardNumber(cardNumber);
        delegate.setCustomerId(customerId);
        delegate.setEnvironment(environment);
        delegate.setRecurringAmount(recurringAmount);
        delegate.setRecurringCount(recurringCount);
        delegate.setRecurringImmediately(recurringImmediately);
        delegate.setRecurringPeriod(recurringPeriod);
        delegate.setRecurringStartDate(recurringStartDate);
        delegate.setRecurringUnit(recurringUnit);
        delegate.setTransaction(transaction);
        delegate.invoke();
    }

    /**
     * Open the payment gateway.
     * 
     */
    private void open() {
        this.state = State.OPEN;
    }

    /** <b>Title:</b>Payment Gateway State<br> */
    private enum State { CLOSED, OPEN }
}
