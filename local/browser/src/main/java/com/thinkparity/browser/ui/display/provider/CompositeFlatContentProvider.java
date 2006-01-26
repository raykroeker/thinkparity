/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.ui.display.provider;

/**
 * Provides multiple flat lists.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class CompositeFlatContentProvider extends ContentProvider {
	public abstract Object[] getElements(final Integer index, final Object input);
}
