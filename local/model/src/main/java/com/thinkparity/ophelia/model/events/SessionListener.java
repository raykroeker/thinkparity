/*
 * May 7, 2005
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.event.EventListener;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface SessionListener extends EventListener {

    /**
     * A session error has occured.
     * 
     * @param cause
     *            The cause <code>Throwable</code>.
     */
    public void sessionError(final Throwable cause);

    /**
     * A session has been established.
     *
     */
	public void sessionEstablished();

    /**
     * The session has been terminated.
     *
     */
	public void sessionTerminated();
}
