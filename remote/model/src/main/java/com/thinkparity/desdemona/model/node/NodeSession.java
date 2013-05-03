/*
 * Created On:  4-Sep-07 1:15:38 PM
 */
package com.thinkparity.desdemona.model.node;

import java.util.Calendar;

/**
 * <b>Title:</b>thinkParity Desdemona Model Node Session<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NodeSession {

    /** A creation date. */
    private Calendar createdOn;

    /** A session id. */
    private String id;

    /**
     * Create NodeSession.
     *
     */
    public NodeSession() {
        super();
    }

    /**
     * Obtain the createdOn.
     *
     * @return A <code>Calendar</code>.
     */
    public Calendar getCreatedOn() {
        return createdOn;
    }

    /**
     * Obtain the id.
     *
     * @return A <code>String</code>.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the createdOn.
     *
     * @param createdOn
     *		A <code>Calendar</code>.
     */
    public void setCreatedOn(final Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Set the id.
     *
     * @param id
     *		A <code>String</code>.
     */
    public void setId(final String id) {
        this.id = id;
    }
}
