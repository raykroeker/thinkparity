/*
 * Jan 25, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class SingleContentProvider extends ContentProvider {

	/**
     * Create SingleContentProvider.
     * 
     * @param profileModel
     *            A thinkParity profileModel.
     */
	protected SingleContentProvider(final ProfileModel profileModel) { super(profileModel); }

	public abstract Object getElement(final Object input);
}
