/*
 * Jan 26, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.codebase.model.profile.Profile;

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
     * @param profile
     *            A thinkParity profile.
     */
    public CompositeFlatSingleContentProvider(final Profile profile) {
        super(profile);
    }

    public abstract Object getElement(final Integer index, final Object input);

    public abstract Object[] getElements(final Integer index, final Object input);
}
