/*
 * Jan 16, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class FlatContentProvider extends ContentProvider {

	/**
     * Create FlatContentProvider.
     * 
     * @param profileModel
     *            A thinkParity profileModel.
     */
	protected FlatContentProvider(final ProfileModel profileModel) {
        super(profileModel);
	}

	/**
	 * Obtain a flat list of elements.
	 * 
	 * @return The list of elements.
	 */
	public abstract Object[] getElements(final Object input);
}
