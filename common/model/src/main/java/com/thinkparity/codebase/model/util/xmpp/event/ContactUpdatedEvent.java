/*
 * Created On:  10-Nov-06 3:11:15 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.annotation.ThinkParityFilterEvent;

/**
 * <b>Title:</b>thinkParity CommonModel Contact Updated Event<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
@ThinkParityFilterEvent
public final class ContactUpdatedEvent extends XMPPEvent {

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