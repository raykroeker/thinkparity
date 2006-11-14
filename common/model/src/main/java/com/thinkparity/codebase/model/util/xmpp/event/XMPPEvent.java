/*
 * Created On:  10-Nov-06 2:07:41 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class XMPPEvent {

    /** The xmpp event date <code>Calendar</code>. */
    private Calendar date;

    /** The xmpp event id <code>String</code>. */
    private String id;

    /**
     * Create XMPPEvent.
     * 
     */
    protected XMPPEvent() {
        super();
    }

    /**
     * Obtain date.
     *
     * @return A Calendar.
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Obtain id.
     *
     * @return A String.
     */
    public String getId() {
        return id;
    }

    /**
     * Set date.
     *
     * @param date
     *		A Calendar.
     */
    public void setDate(final Calendar date) {
        this.date = date;
    }

    /**
     * Set id.
     *
     * @param id
     *		A String.
     */
    public void setId(final String id) {
        this.id = id;
    }
}
