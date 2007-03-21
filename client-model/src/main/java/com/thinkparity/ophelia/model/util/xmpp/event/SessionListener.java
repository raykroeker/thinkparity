/*
 * Feb 5, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp.event;

import com.thinkparity.codebase.event.EventListener;


/**
 * XMPPSessionListener
 * This interface is used when a client is interested in knowing about the
 * establish\terminate events for a session.  To make use of this inteface, implement it
 * then add it to a session.
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public interface SessionListener extends EventListener {

	/**
     * A session error has occured.
     * 
     * @param t
     *            A <code>Throwable</code> error.
     */
    public void sessionError(final Throwable t);

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
