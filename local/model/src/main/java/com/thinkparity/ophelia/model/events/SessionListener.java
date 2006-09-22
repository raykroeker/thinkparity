/*
 * May 7, 2005
 */
package com.thinkparity.ophelia.model.events;

import com.thinkparity.codebase.event.EventListener;

/**
 * SessionListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface SessionListener extends EventListener {
	public void sessionEstablished();
	public void sessionTerminated();
	public void sessionTerminated(final Throwable cause);
}
