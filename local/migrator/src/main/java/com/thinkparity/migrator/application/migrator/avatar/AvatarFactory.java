/*
 * Created On: Jun 25, 2006 11:43:22 AM
 * $Id$
 */
package com.thinkparity.migrator.application.migrator.avatar;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.migrator.application.migrator.Application;


/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class AvatarFactory {

    private static final AvatarFactory SINGLETON;

    static { SINGLETON = new AvatarFactory(); }

    public static Avatar create(final Application application, final AvatarId id) {
        synchronized(SINGLETON) { return SINGLETON.doCreate(application, id); }
    }

    /** The avatar registry. */
    private final AvatarRegistry registry;

    /** Create AvatarFactory. */
    private AvatarFactory() {
        super();
        this.registry = new AvatarRegistry();
    }

    private Avatar doCreate(final Application application, final AvatarId id) {
        final Avatar avatar;
        switch(id) {
        case RELEASE_SUMMARY:
            avatar = new com.thinkparity.migrator.application.migrator.avatar.ReleaseSummary(application);
            avatar.setContentProvider(ContentProviderFactory.create(id));
            break;
        default: throw Assert.createUnreachable("");
        }
        register(avatar);
        return avatar;
    }

    private void register(final Avatar avatar) {
        registry.register(avatar);
    }
}
