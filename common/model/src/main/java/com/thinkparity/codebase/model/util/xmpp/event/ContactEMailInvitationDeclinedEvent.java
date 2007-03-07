/*
 * Created On:  10-Nov-06 3:11:25 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactEMailInvitationDeclinedEvent extends XMPPEvent {

    /** The declined by user id <code>JabberId</code>. */
    private JabberId declinedBy;

    /** The declined on date <code>Calendar</code>. */
    private Calendar declinedOn;

    /** The invited as <code>EMail</code>. */
    private EMail invitedAs;

    /**
     * Create ContactInvitationDeclinedEvent.
     *
     */
    public ContactEMailInvitationDeclinedEvent() {
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
     * Obtain invitedAs.
     *
     * @return A EMail.
     */
    public EMail getInvitedAs() {
        return invitedAs;
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

    /**
     * Set invitedAs.
     *
     * @param invitedAs
     *		A EMail.
     */
    public void setInvitedAs(final EMail invitedAs) {
        this.invitedAs = invitedAs;
    }
}