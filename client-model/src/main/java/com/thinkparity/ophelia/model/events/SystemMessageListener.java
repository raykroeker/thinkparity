/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.events;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface SystemMessageListener {
	public void systemMessageCreated(final SystemMessageEvent systemMessageEvent);
}
