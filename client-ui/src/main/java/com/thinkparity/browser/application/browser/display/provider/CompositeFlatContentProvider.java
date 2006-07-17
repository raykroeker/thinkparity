/*
 * Jan 26, 2006
 */
package com.thinkparity.browser.application.browser.display.provider;

import com.thinkparity.model.parity.model.profile.Profile;

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
     * @param profile
     *            A thinkParity profile.
     */
    public CompositeFlatContentProvider(final Profile profile) {
        super(profile);
    }

    public abstract Object[] getElements(final Integer index, final Object input);
}
