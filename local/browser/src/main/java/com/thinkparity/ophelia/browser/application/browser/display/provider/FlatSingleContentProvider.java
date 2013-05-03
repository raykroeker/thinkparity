/**
 * Created On: 4-Jul-2006 1:52:24 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider;

import com.thinkparity.ophelia.model.profile.ProfileModel;

/**
 * @author rob_masako@shaw.ca - Updated by raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class FlatSingleContentProvider extends ContentProvider {

    /**
     * Create a FlatSingleContentProvider.
     * 
     * @param profileModel
     *            A thinkParity profileModel.
     */
    protected FlatSingleContentProvider(final ProfileModel profileModel) { super(profileModel); }

    /**
     * Obtain a single element.
     *
     * @return The element.
     */
    public abstract Object getElement(final Object input);
    
    /**
     * Obtain a flat list of elements.
     * 
     * @return The list of elements.
     */
    public abstract Object[] getElements(final Object input);
}
