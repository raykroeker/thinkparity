/*
 * Created On: Oct 20, 2006 12:55
 */
package com.thinkparity.cordelia.ui.application;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.ui.application.Application;
import com.thinkparity.codebase.ui.application.ApplicationId;

import com.thinkparity.cordelia.ui.CordeliaPlatform;
import com.thinkparity.cordelia.ui.application.ApplicationRegistry.Id;
import com.thinkparity.cordelia.ui.application.admin.AdminApplication;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ApplicationFactory
        extends
        com.thinkparity.codebase.ui.application.ApplicationFactory<CordeliaPlatform> {

    /**
     * Create ApplicationFactory.
     * 
     */
    public ApplicationFactory(final CordeliaPlatform platform) {
        super(new ApplicationRegistry(platform));
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.ApplicationFactory#doCreate(com.thinkparity.codebase.ui.application.ApplicationId)
     */
    @Override
    protected Application<CordeliaPlatform> doCreate(final ApplicationId id) {
        switch ((Id) id) {
        case ADMIN:
            return AdminApplication.getInstance();
        default:
            throw Assert.createUnreachable("UNKNOWN APPLICATION {0}", id);
        }
    }
}
