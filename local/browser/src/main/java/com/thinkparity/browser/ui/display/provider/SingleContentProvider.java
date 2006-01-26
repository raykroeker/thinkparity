/*
 * Jan 25, 2006
 */
package com.thinkparity.browser.ui.display.provider;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class SingleContentProvider extends ContentProvider {

	/**
	 * Create a SingleContentProvider.
	 */
	protected SingleContentProvider() { super(); }

	public abstract Object getElement(final Object input);
}
