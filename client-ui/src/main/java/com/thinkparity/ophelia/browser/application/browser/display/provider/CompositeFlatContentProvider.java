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
public abstract class CompositeFlatContentProvider extends ContentProvider {

    /**
     * Create CompositeFlatContentProvider.
     * 
     * @param profileModel
     *            A thinkParity profileModel.
     */
    public CompositeFlatContentProvider(final ProfileModel profileModel) {
        super(profileModel);
    }

    public abstract Object[] getElements(final Integer index, final Object input);
}
