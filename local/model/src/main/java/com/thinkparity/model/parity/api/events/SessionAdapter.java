/*
 * Created On: Aug 8, 2006 5:17:29 PM
 */
package com.thinkparity.model.parity.api.events;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class SessionAdapter implements SessionListener {

    /**
     * @see com.thinkparity.model.parity.api.events.SessionListener#sessionEstablished()
     */
    public void sessionEstablished() {}

    /**
     * @see com.thinkparity.model.parity.api.events.SessionListener#sessionTerminated()
     */
    public void sessionTerminated() {}

    /**
     * @see com.thinkparity.model.parity.api.events.SessionListener#sessionTerminated(java.lang.Throwable)
     */
    public void sessionTerminated(final Throwable cause) {}
}
