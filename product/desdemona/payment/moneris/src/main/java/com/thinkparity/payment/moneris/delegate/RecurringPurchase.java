/*
 * Created On:  4-Sep-07 10:28:19 PM
 */
package com.thinkparity.payment.moneris.delegate;

import JavaAPI.Receipt;
import JavaAPI.Recur;

/**
 * <b>Title:</b>thinkParity Moneris Payment Purchase Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RecurringPurchase extends MonerisDelegate {

    /** The authorization amount. */
    private String amount;

    /** The card expiry date. */
    private String cardExpiryDate;

    /** The card number. */
    private String cardNumber;

    /** The customer id. */
    private String customerId;

    /** The recurring amount. */
    private String recurringAmount;

    /** The recurring count. */
    private String recurringCount;

    /** The recurring immediately. */
    private String recurringImmediately;

    /** The recurring period. */
    private String recurringPeriod;

    /** The recurring start date. */
    private String recurringStartDate;

    /** The recurring unit. */
    private String recurringUnit;

    /**
     * Create RecurringPurchase.
     *
     */
    public RecurringPurchase() {
        super();
    }

    /**
     * @see com.thinkparity.payment.moneris.delegate.MonerisDelegate#invoke()
     *
     */
    @Override
    public void invoke() throws Exception {
        post(newRecurringPurchase());
        final Receipt receipt = getReceipt();
        final String recurSuccess = receipt.getRecurSuccess();
        if (Boolean.valueOf(recurSuccess)) {
        } else {
            throw panic();
        }
    }

    /**
     * Set the amount.
     *
     * @param amount
     *      A <code>String</code>.
     */
    public void setAmount(final String amount) {
        this.amount = amount;
    }

    /**
     * Set the cardExpiryDate.
     *
     * @param cardExpiryDate
     *      A <code>String</code>.
     */
    public void setCardExpiryDate(final String cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    /**
     * Set the cardNumber.
     *
     * @param cardNumber
     *      A <code>String</code>.
     */
    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * Set the customerId.
     *
     * @param customerId
     *		A <code>String</code>.
     */
    public void setCustomerId(final String customerId) {
        this.customerId = customerId;
    }

    /**
     * Set the recurringAmount.
     *
     * @param recurringAmount
     *		A <code>String</code>.
     */
    public void setRecurringAmount(final String recurringAmount) {
        this.recurringAmount = recurringAmount;
    }

    /**
     * Set the recurringCount.
     *
     * @param recurringCount
     *		A <code>String</code>.
     */
    public void setRecurringCount(final String recurringCount) {
        this.recurringCount = recurringCount;
    }

    /**
     * Set the recurringImmediately.
     *
     * @param recurringImmediately
     *		A <code>String</code>.
     */
    public void setRecurringImmediately(final String recurringImmediately) {
        this.recurringImmediately = recurringImmediately;
    }

    /**
     * Set the recurringPeriod.
     *
     * @param recurringPeriod
     *		A <code>String</code>.
     */
    public void setRecurringPeriod(final String recurringPeriod) {
        this.recurringPeriod = recurringPeriod;
    }

    /**
     * Set the recurringStartDate.
     *
     * @param recurringStartDate
     *		A <code>String</code>.
     */
    public void setRecurringStartDate(final String recurringStartDate) {
        this.recurringStartDate = recurringStartDate;
    }

    /**
     * Set the recurringUnit.
     *
     * @param recurringUnit
     *		A <code>String</code>.
     */
    public void setRecurringUnit(final String recurringUnit) {
        this.recurringUnit = recurringUnit;
    }

    /**
     * Create a moneris recur.
     * 
     * @return A <code>Recur</code>.
     */
    private Recur newRecur() {
        final Recur recur = new Recur();
        recur.setRecurUnit(recurringUnit);
        recur.setStartDate(recurringStartDate);
        recur.setStartNow(recurringImmediately);
        recur.setNumRecurs(recurringCount);
        recur.setPeriod(recurringPeriod);
        recur.setRecurAmount(recurringAmount);
        return recur;
    }

    /**
     * Create a moneris purchase.
     * 
     * @return A <code>JavaAPI.Purchase</code>.
     */
    private JavaAPI.Purchase newRecurringPurchase() {
        final JavaAPI.Purchase purchase = new JavaAPI.Purchase(
                getTransaction().getId(), amount, cardNumber, cardExpiryDate,
                getEnvironment().getEncryption(), newRecur());
        purchase.setCustId(customerId);
        return purchase;
    }
}
