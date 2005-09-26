/*
 * May 7, 2005
 */
package com.thinkparity.model.parity.api.events;

/**
 * SessionListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface SessionListener {

	public void sessionEstablished();

	public void sessionTerminated();

	public void sessionTerminated(final Throwable cause);
}
