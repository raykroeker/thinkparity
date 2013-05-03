/**
 * 
 */
package com.thinkparity.cordelia.ui.application.admin;

import com.thinkparity.codebase.ui.application.ApplicationId;
import com.thinkparity.codebase.ui.application.ApplicationStatus;

import com.thinkparity.cordelia.ui.CordeliaPlatform;
import com.thinkparity.cordelia.ui.application.Application;
import com.thinkparity.cordelia.ui.application.ApplicationRegistry.Id;

/**
 * @author raymond
 *
 */
public final class AdminApplication extends Application {

    private static final AdminApplication INSTANCE;

    static {
        INSTANCE = new AdminApplication();
    }

    public static AdminApplication getInstance() {
        return INSTANCE;
    }

    /** The <code>AdminWindow</code>. */
    private AdminWindow adminWindow;

    /**
     * Create AdminApplication.
     * 
     */
    private AdminApplication() {
        super();
    }

    /**
     * @see com.thinkparity.codebase.ui.application.Application#end(com.thinkparity.codebase.ui.platform.Platform)
     * 
     */
    public void end(final CordeliaPlatform platform) {
    }

    /**
     * @see com.thinkparity.codebase.ui.application.Application#getId()
     * 
     */
    public ApplicationId getId() {
        return Id.ADMIN;
    }

    /**
     * @see com.thinkparity.codebase.ui.application.Application#hibernate(com.thinkparity.codebase.ui.platform.Platform)
     */
    public void hibernate(final CordeliaPlatform platform) {
    }

    /**
     * @see com.thinkparity.codebase.ui.application.Application#restore(com.thinkparity.codebase.ui.platform.Platform)
     * 
     */
    public void restore(final CordeliaPlatform platform) {
    }

    /**
     * @see com.thinkparity.codebase.ui.application.Application#start(com.thinkparity.codebase.ui.platform.Platform)
     * 
     */
    public void start(final CordeliaPlatform platform) {
        logger.logApiId();
        setStatus(ApplicationStatus.STARTING);
        openAdminWindow();
        setStatus(ApplicationStatus.RUNNING);
    }

    private void openAdminWindow() {
        adminWindow = new AdminWindow();
        adminWindow.open();
    }
}
