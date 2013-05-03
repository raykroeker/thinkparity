/*
 * Created On:  10-Nov-06 3:09:46 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityFilterEvent;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@ThinkParityFilterEvent
public final class ContactUserInvitationExtendedEvent extends XMPPEvent {

    /** The invited by user id <code>JabberId</code>. */
    private JabberId invitedBy;

    /** The invited on date <code>Calendar</code>. */
    private Calendar invitedOn;

    /**
     * Create ContactInvitationExtendedEvent.
     *
     */
    public ContactUserInvitationExtendedEvent() {
        super();
    }

    /**
     * Obtain invitedBy.
     *
     * @return A JabberId.
     */
    public JabberId getInvitedBy() {
        return invitedBy;
    }

    /**
     * Obtain invitedOn.
     *
     * @return A Calendar.
     */
    public Calendar getInvitedOn() {
        return invitedOn;
    }

    /**
     * Set invitedBy.
     *
     * @param invitedBy
     *		A JabberId.
     */
    public void setInvitedBy(final JabberId invitedBy) {
        this.invitedBy = invitedBy;
    }

    /**
     * Set invitedOn.
     *
     * @param invitedOn
     *		A Calendar.
     */
    public void setInvitedOn(final Calendar invitedOn) {
        this.invitedOn = invitedOn;
    }
}