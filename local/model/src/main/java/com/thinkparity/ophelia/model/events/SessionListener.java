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
	public void sessionEstablished();
	public void sessionTerminated();
	public void sessionTerminated(final Throwable cause);
    public void sessionError(final Throwable cause);
}
