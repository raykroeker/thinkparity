/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import com.thinkparity.model.parity.model.profile.Profile;

/**
 * Provides multiple objects.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public abstract class CompositeSingleContentProvider extends ContentProvider {

    /**
     * Create CompositeSingleContentProvider.
     * 
     * @param profile
     *            A thinkParity profile.
     */
    public CompositeSingleContentProvider(final Profile profile) {
        super(profile);
    }

    public abstract Object getElement(final Integer index, final Object input);
}
