/*
 * Jan 26, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * Provides multiple flat lists.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class CompositeFlatSingleContentProvider extends ContentProvider {

	/**
     * Create CompositeFlatSingleContentProvider.
     * 
     * @param profileModel
     *            A thinkParity profileModel.
     */
    public CompositeFlatSingleContentProvider(final ProfileModel profileModel) {
        super(profileModel);
    }

    public abstract Object getElement(final Integer index, final Object input);

    public abstract Object[] getElements(final Integer index, final Object input);
}
