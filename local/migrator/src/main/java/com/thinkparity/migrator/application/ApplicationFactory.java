/*
 * Created On: Sun Jun 25 2006 10:56 PDT
 * $Id$
 */
package com.thinkparity.migrator.application;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.migrator.platform.Platform;

public class ApplicationFactory {

    /** The factory singleton. */
    private static final ApplicationFactory SINGLETON;

    static { SINGLETON = new ApplicationFactory(); }

    /**
     * Create an application.
     * 
     * @param platform
     *            The migrator platform.
     * @param id
     *            The application id.
     * @return The application.
     */
    public static Application create(final Platform platform,
            final ApplicationId id) {
        synchronized(SINGLETON) { return SINGLETON.doCreate(platform, id); }
    }

    /** Create ApplicationFactory. */
    private ApplicationFactory() { super(); }

    /**
     * Create an application.
     * 
     * @param platform
     *            The migrator platform.
     * @param id
     *            The application id.
     * @return The application.
     */
    private Application doCreate(final Platform platform, final ApplicationId id) {
        final Application application;
        switch(id) {
            case MIGRATOR:
                application = new com.thinkparity.migrator.application.migrator.Application(platform);
                break;
            default: throw Assert.createUnreachable("");
        }
        register(application);
        return application;
    }

    /**
     * Register the application in the registry.
     *
     * @param application
     *      An application.
     */
    private void register(final Application application) {}
}
