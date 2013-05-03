/*
 * Created On:  10-Nov-06 3:11:11 PM
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
public final class ContactDeletedEvent extends XMPPEvent {

    /** The deleted by user id <code>JabberId</code>. */
    private JabberId deletedBy;

    /** The deleted on date <code>Calendar</code>. */
    private Calendar deletedOn;

    /**
     * Create ContactDeletedEvent.
     *
     */
    public ContactDeletedEvent() {
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
}