/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.platform;

import com.thinkparity.browser.platform.util.State;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Saveable {
	public void save(final State state);
	public void restore(final State state);
}
