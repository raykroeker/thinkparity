/*
 * Created On: Aug 8, 2006 5:17:29 PM
 */
package com.thinkparity.ophelia.model.events;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class SessionAdapter implements SessionListener {

    /**
     * @see com.thinkparity.ophelia.model.events.SessionListener#sessionEstablished()
     */
    public void sessionEstablished() {}

    /**
     * @see com.thinkparity.ophelia.model.events.SessionListener#sessionTerminated()
     */
    public void sessionTerminated() {}

    /**
     * @see com.thinkparity.ophelia.model.events.SessionListener#sessionTerminated(java.lang.Throwable)
     */
    public void sessionTerminated(final Throwable cause) {}

    /**
     * @see com.thinkparity.ophelia.model.events.SessionListener#sessionError(java.lang.Throwable)
     * 
     */
    public void sessionError(final Throwable cause) {}
}
