/*
 * Created On:  10-Nov-06 3:09:46 PM
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
public final class ContactInvitationExtendedEvent extends XMPPEvent {

    /** The original invitation <code>EMail</code>. */
    private EMail invitedAs;

    /** The invited by user id <code>JabberId</code>. */
    private JabberId invitedBy;

    /** The invited on date <code>Calendar</code>. */
    private Calendar invitedOn;

    /**
     * Create ContactInvitationExtendedEvent.
     *
     */
    public ContactInvitationExtendedEvent() {
        super();
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
     * Set invitedAs.
     *
     * @param invitedAs
     *		A EMail.
     */
    public void setInvitedAs(final EMail invitedAs) {
        this.invitedAs = invitedAs;
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