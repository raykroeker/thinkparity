/*
 * Created On: Jul 13, 2006 9:44:34 AM
 */
package com.thinkparity.codebase.model.session;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * <b>Title:</b>thinkParity User Session<br>
 * <b>Description:</b>The thinkParity user's session. Contains session
 * information for the logged in user.
 * 
 * @author raymond@thinkparity.com
 * @version
 */
public final class Session {

    private JabberId jabberId;

    /** Create Session. */
    public Session() { super(); }

    /**
     * Obtain the jabber id.
     * 
     * @return The jabber id.
     */
    public JabberId getJabberId() { return jabberId; }

    /**
     * Set the jabber id.
     * 
     * @param jabberId
     *            The jabber id.
     */
    public void setJabberId(final JabberId jabberId) {
        this.jabberId = jabberId;
    }
}
