/*
 * Created On:  28-Sep-07 3:34:22 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event.profile.payment;

import java.util.Calendar;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity Common Model Profile Payment Plan Arrears Event<br>
 * <b>Description:</b>This event is fired to all profiles attached to a given
 * plan when a payment plan is in arrears.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentPlanArrearsEvent extends XMPPEvent {

    /** An in/out of arrears on date. */
    private Calendar arrearsOn;

    /** An indicator as to whether or not the plan is in arrears. */
    private Boolean inArrears;

    /**
     * Create PaymentPlanArrearsEvent.
     *
     */
    public PaymentPlanArrearsEvent() {
        super();
    }

    /**
     * Determine if the plan is in arrears.
     * 
     * @return True if the plan is inarrears.
     */
    public Boolean isInArrears(){ 
        return inArrears;
    }

    /**
     * Set the arrearsOn.
     *
     * @param arrearsOn
     *		A <code>Calendar</code>.
     */
    public void setArrearsOn(final Calendar arrearsOn) {
        this.arrearsOn = arrearsOn;
    }

    /**
     * Set the inArrears.
     *
     * @param inArrears
     *		A <code>Boolean</code>.
     */
    public void setInArrears(final Boolean inArrears) {
        this.inArrears = inArrears;
    }

    /**
     * Obtain the arrearsOn.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getArrearsOn() {
        return arrearsOn;
    }

}
