/*
 * Created On: Jul 13, 2006 9:44:34 AM
 */
package com.thinkparity.model.parity.model.session;

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
    Session() { super(); }

    /**
     * Set the jabber id.
     * 
     * @param jabberId
     *            The jabber id.
     */
    void setJabberId(final JabberId jabberId) { this.jabberId = jabberId; }

    /**
     * Obtain the jabber id.
     * 
     * @return The jabber id.
     */
    public JabberId getJabberId() { return jabberId; }
}
