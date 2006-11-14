/*
 * Created On:  10-Nov-06 3:11:15 PM
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
public class ContactUpdatedEvent extends XMPPEvent {

    /** The contact id <code>JabberId</code>. */
    private JabberId contactId;

    /** The updated on date <code>Calendar</code>. */
    private Calendar updatedOn;

    /**
     * Create ContactUpdatedEvent.
     *
     */
    public ContactUpdatedEvent() {
        super();
    }

    /**
     * Obtain contactId.
     *
     * @return A JabberId.
     */
    public JabberId getContactId() {
        return contactId;
    }

    /**
     * Obtain updatedOn.
     *
     * @return A Calendar.
     */
    public Calendar getUpdatedOn() {
        return updatedOn;
    }

    /**
     * Set contactId.
     *
     * @param contactId
     *		A JabberId.
     */
    public void setContactId(final JabberId contactId) {
        this.contactId = contactId;
    }

    /**
     * Set updatedOn.
     *
     * @param updatedOn
     *		A Calendar.
     */
    public void setUpdatedOn(final Calendar updatedOn) {
        this.updatedOn = updatedOn;
    }
}