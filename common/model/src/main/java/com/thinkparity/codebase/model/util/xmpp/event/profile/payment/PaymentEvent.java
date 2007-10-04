/*
 * Created On:  28-Sep-07 10:10:53 AM
 */
package com.thinkparity.codebase.model.util.xmpp.event.profile.payment;

import java.util.Calendar;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity Common Model Profile Payment Failed Event<br>
 * <b>Description:</b>An even that is fired if the user's payment has failed.
 * This event is distributed to the payment plan's owner only.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentEvent extends XMPPEvent {

    /** When the payment succeeded/failed. */
    private Calendar paymentOn;

    /** A success/fail flag. */
    private Boolean success;

    /**
     * Create PaymentFailedEvent.
     *
     */
    public PaymentEvent() {
        super();
    }

    /**
     * Obtain the paymentOn.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getPaymentOn() {
        return paymentOn;
    }

    /**
     * Obtain the success.
     *
     * @return A <code>Boolean</code>.
     */
    public Boolean isSuccess() {
        return success;
    }

    /**
     * Set the paymentOn.
     *
     * @param paymentOn
     *		A <code>Calendar</code>.
     */
    public void setPaymentOn(final Calendar paymentOn) {
        this.paymentOn = paymentOn;
    }

    /**
     * Set the success.
     *
     * @param success
     *		A <code>Boolean</code>.
     */
    public void setSuccess(final Boolean success) {
        this.success = success;
    }
}
