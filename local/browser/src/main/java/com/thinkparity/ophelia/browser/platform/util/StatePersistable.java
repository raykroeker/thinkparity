/*
 * Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.platform.util;

/**
 * The state persistance of a class is achieved by implementing this interface.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 * @see State#save
 * @see State#
 */
public interface StatePersistable {
	public State getState();
	public void setState(final State state);
}
