/*
 * Created On:  10-Nov-06 2:07:41 PM
 */
package com.thinkparity.codebase.model.util.xmpp.event;

import java.util.Calendar;

/**
 * <b>Title:</b>thinkParity CommonModel XMPP Event<br>
 * <b>Description:</b>An xmpp event.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public abstract class XMPPEvent {

    /** The xmpp event date <code>Calendar</code>. */
    private Calendar date;

    /** The xmpp event id <code>String</code>. */
    private String id;

    /** The xmpp event priority. */
    private Priority priority;

    /**
     * Create XMPPEvent.
     * 
     */
    public XMPPEvent() {
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
     * Obtain priority.
     *
     * @return A Priority.
     */
    public Priority getPriority() {
        return priority;
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

    /**
     * Set priority.
     *
     * @param priority
     *		A Priority.
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * <b>Title:</b>XMPPEvent Priority<br>
     * <b>Description:</b>The event priority class.<br>
     */
    public enum Priority {

        HIGH(10), LOW(-10), NORMAL(0);

        /** An <code>Integer</code> priority. */
        private Integer priority;

        /**
         * Create Priority.
         * 
         * @param priority
         *            An <code>Integer</code> priority.
         */
        private Priority(final Integer priority) {
            this.priority = priority;
        }

        /**
         * Obtain the priority. This is used when storing a priority.
         * 
         * @return The <code>Integer</code> priority.
         */
        public Integer priority() {
            return priority;
        }
    }
}
