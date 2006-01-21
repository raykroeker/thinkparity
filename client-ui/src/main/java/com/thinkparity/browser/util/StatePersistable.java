/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.util;

/**
 * The state persistance of a class is achieved by implementing this interface.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see State#save
 * @see State#
 */
public interface StatePersistable {
	public State getState();
	public void setState(final State state);
}
