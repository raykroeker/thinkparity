/*
 * Created On:  29-May-07 8:14:41 PM
 */
package com.thinkparity.desdemona.model.session;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalSessionModel extends SessionModel {

    /**
     * Read the user's session.
     * 
     * @return A <code>Session</code>.
     */
    public Session read();
}
