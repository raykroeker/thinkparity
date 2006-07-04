/**
 * Created On: 4-Jul-2006 1:52:24 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.provider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class FlatSingleContentProvider extends ContentProvider {

    /**
     * Create a FlatSingleContentProvider.
     */
    protected FlatSingleContentProvider() { super(); }

    /**
     * Obtain a flat list of elements.
     * 
     * @return The list of elements.
     */
    public abstract Object[] getElements(final Object input);
    
    /**
     * Obtain a single element.
     *
     * @return The element.
     */
    
    public abstract Object getElement(final Object input);
}
