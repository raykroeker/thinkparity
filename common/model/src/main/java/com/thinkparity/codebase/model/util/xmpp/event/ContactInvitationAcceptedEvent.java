/*
 * Created On:  10-Nov-06 3:11:19 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactInvitationAcceptedEvent extends XMPPEvent {

    /** The accepted by user id <code>JabberId</code>. */
    private JabberId acceptedBy;

    /** The accepted on date <code>Calendar</code>. */
    private Calendar acceptedOn;

    /**
     * Create ContactInvitationAcceptedEvent.
     *
     */
    public ContactInvitationAcceptedEvent() {
        super();
    }

    /**
     * Obtain acceptedBy.
     *
     * @return A JabberId.
     */
    public JabberId getAcceptedBy() {
        return acceptedBy;
    }

    /**
     * Obtain acceptedOn.
     *
     * @return A Calendar.
     */
    public Calendar getAcceptedOn() {
        return acceptedOn;
    }

    /**
     * Set acceptedBy.
     *
     * @param acceptedBy
     *		A JabberId.
     */
    public void setAcceptedBy(final JabberId acceptedBy) {
        this.acceptedBy = acceptedBy;
    }

    /**
     * Set acceptedOn.
     *
     * @param acceptedOn
     *		A Calendar.
     */
    public void setAcceptedOn(final Calendar acceptedOn) {
        this.acceptedOn = acceptedOn;
    }
}