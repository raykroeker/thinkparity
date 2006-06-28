/*
 * Created On: Jun 25, 2006 1:27:27 PM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator.avatar;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.release.ReleaseModel;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ContentProviderFactory {

    private static final ContentProviderFactory SINGLETON;

    static { SINGLETON = new ContentProviderFactory(); }

    public static ContentProvider create(final AvatarId avatarId) {
        synchronized(SINGLETON) { return SINGLETON.doCreate(avatarId); }
    }

    /** A thinkParity release interface. */
    private final ReleaseModel rModel;

    /** Create ContentProviderFactory. */
    private ContentProviderFactory() {
        super();
        this.rModel = ReleaseModel.getModel();
    }

    /**
     * Create a content provider for an avatar.
     * 
     * @param avatarId
     *            An avatar id.
     * @return A content provider.
     */
    private ContentProvider doCreate(final AvatarId avatarId) {
        final ContentProvider contentProvider;
        switch(avatarId) {
        case RELEASE_SUMMARY:
            contentProvider = new ReleaseSummaryContentProvider(rModel);
            break;
        default: throw Assert.createUnreachable("");
        }
        return contentProvider;
    }
}
