/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class FlatContentProvider extends ContentProvider {

	/**
     * Create FlatContentProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     */
	protected FlatContentProvider(final Profile profile) { super(profile); }

	/**
	 * Obtain a flat list of elements.
	 * 
	 * @return The list of elements.
	 */
	public abstract Object[] getElements(final Object input);
}
