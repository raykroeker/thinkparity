/*
 * Created On:  10-Nov-06 3:11:25 PM
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
public final class ContactUserInvitationDeclinedEvent extends XMPPEvent {

    /** The declined by user id <code>JabberId</code>. */
    private JabberId declinedBy;

    /** The declined on date <code>Calendar</code>. */
    private Calendar declinedOn;

    /**
     * Create ContactInvitationDeclinedEvent.
     *
     */
    public ContactUserInvitationDeclinedEvent() {
        super();
    }

    /**
     * Obtain declinedBy.
     *
     * @return A JabberId.
     */
    public JabberId getDeclinedBy() {
        return declinedBy;
    }

    /**
     * Obtain declinedOn.
     *
     * @return A Calendar.
     */
    public Calendar getDeclinedOn() {
        return declinedOn;
    }

    /**
     * Set declinedBy.
     *
     * @param declinedBy
     *		A JabberId.
     */
    public void setDeclinedBy(final JabberId declinedBy) {
        this.declinedBy = declinedBy;
    }

    /**
     * Set declinedOn.
     *
     * @param declinedOn
     *		A Calendar.
     */
    public void setDeclinedOn(final Calendar declinedOn) {
        this.declinedOn = declinedOn;
    }
}