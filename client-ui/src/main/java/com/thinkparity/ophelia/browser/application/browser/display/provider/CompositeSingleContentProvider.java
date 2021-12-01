/*
 * Jan 26, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * Provides multiple objects.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1.2.1
 */
public abstract class CompositeSingleContentProvider extends ContentProvider {

    /**
     * Create CompositeSingleContentProvider.
     * 
     * @param profileModel
     *            A thinkParity profileModel.
     */
    public CompositeSingleContentProvider(final ProfileModel profileModel) {
        super(profileModel);
    }

    public abstract Object getElement(final Integer index, final Object input);
}
