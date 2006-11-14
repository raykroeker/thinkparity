/*
 * Created On:  10-Nov-06 3:11:29 PM
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
public final class ContactInvitationDeletedEvent extends XMPPEvent {

    /** The deleted by user id <code>JabberId</code>. */
    private JabberId deletedBy;

    /** The deleted on date <code>Calendar</code>. */
    private Calendar deletedOn;

    /** The invited as <code>EMail</code>. */
    private EMail invitedAs;

    /**
     * Create ContactInvitationDeletedEvent.
     *
     */
    public ContactInvitationDeletedEvent() {
        super();
    }

    /**
     * Obtain deletedBy.
     *
     * @return A JabberId.
     */
    public JabberId getDeletedBy() {
        return deletedBy;
    }

    /**
     * Obtain deletedOn.
     *
     * @return A Calendar.
     */
    public Calendar getDeletedOn() {
        return deletedOn;
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
     * Set deletedBy.
     *
     * @param deletedBy
     *		A JabberId.
     */
    public void setDeletedBy(final JabberId deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     * Set deletedOn.
     *
     * @param deletedOn
     *		A Calendar.
     */
    public void setDeletedOn(final Calendar deletedOn) {
        this.deletedOn = deletedOn;
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