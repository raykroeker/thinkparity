/*
 * Jan 25, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class CompositeSingleContentProvider extends ContentProvider {
	public abstract Object getElement(final Integer index, final Object input);
}
