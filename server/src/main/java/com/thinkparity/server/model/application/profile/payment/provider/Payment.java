/*
 * Created On:  9-Aug-07 4:00:07 PM
 */
package com.thinkparity.desdemona.model.profile.payment.provider;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Payment Provider Payment<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Payment {

    /** The payment amount. */
    private Long amount;

    /**
     * Create Payment.
     *
     */
    public Payment() {
        super();
    }

    /**
     * Obtain the amount.
     *
     * @return A <code>Long</code>.
     */
    public Long getAmount() {
        return amount;
    }

    /**
     * Set the amount.
     *
     * @param amount
     *		A <code>Long</code>.
     */
    public void setAmount(final Long amount) {
        this.amount = amount;
    }
}
