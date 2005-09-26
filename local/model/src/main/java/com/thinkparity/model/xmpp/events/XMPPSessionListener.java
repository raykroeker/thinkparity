/*
 * Feb 5, 2005
 */
package com.thinkparity.model.xmpp.events;

/**
 * XMPPSessionListener
 * This interface is used when a client is interested in knowing about the
 * establish\terminate events for a session.  To make use of this inteface, implement it
 * then add it to a session.
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public interface XMPPSessionListener {

	/**
	 * The session established event was fired.
	 */
	public void sessionEstablished();

	/**
	 * The session terminated event was fired.
	 */
	public void sessionTerminated();

	/**
	 * The session terminated event was fired with an error.
	 * @param x <code>Exception</code>
	 */
	public void sessionTerminated(Exception x);
}
