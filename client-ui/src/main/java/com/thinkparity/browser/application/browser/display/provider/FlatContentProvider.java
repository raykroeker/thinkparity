/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class FlatContentProvider extends ContentProvider {

	/**
	 * Create a FlatContentProvider.
	 */
	protected FlatContentProvider() { super(); }

	/**
	 * Obtain a flat list of elements.
	 * 
	 * @return The list of elements.
	 */
	public abstract Object[] getElements(final Object input);
}
