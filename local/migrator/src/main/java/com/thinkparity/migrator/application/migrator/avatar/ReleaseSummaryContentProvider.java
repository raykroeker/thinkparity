/*
 * Created On: Jun 25, 2006 1:29:46 PM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator.avatar;

import com.thinkparity.model.parity.model.release.ReleaseModel;

import com.thinkparity.migrator.Release;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReleaseSummaryContentProvider extends ContentProviderFlat {

    /** The thinkParity release interface. */
    private final ReleaseModel rModel;

    /** Create ReleaseSummaryContentProvider. */
    public ReleaseSummaryContentProvider(final ReleaseModel rModel) {
        super();
        this.rModel = rModel;
    }

    /**
     * @see com.thinkparity.migrator.application.migrator.avatar.ContentProviderFlat#getElements(java.lang.Object)
     * 
     */
    public Object[] getElements(final Object input) {
        return rModel.readAll().toArray(new Release[] {});
    }
}
