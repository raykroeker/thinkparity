/*
 * Created On: Jun 25, 2006 1:12:01 PM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator.avatar;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ContentProviderFlat extends ContentProvider {

    /**
     * Create a FlatContentProvider.
     */
    protected ContentProviderFlat() { super(); }

    /**
     * Obtain a flat list of elements.
     * 
     * @return The list of elements.
     */
    public abstract Object[] getElements(final Object input);
}
