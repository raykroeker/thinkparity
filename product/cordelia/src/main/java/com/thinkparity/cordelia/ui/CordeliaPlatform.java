/*
 * Created On: Oct 20 2006 12:14
 */
package com.thinkparity.cordelia.ui;

import com.thinkparity.codebase.ApplicationId;
import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.ui.application.Application;
import com.thinkparity.codebase.ui.platform.DefaultPlatform;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.cordelia.ui.application.ApplicationFactory;
import com.thinkparity.cordelia.ui.application.ApplicationRegistry.Id;

/**
 * @author raymond@thikparity.com
 * @version 1.1.2.1
 */
public final class CordeliaPlatform extends DefaultPlatform {

    /** A singleton instance of the platform. */
    private static CordeliaPlatform PLATFORM;

    /**
     * Create an instance of the platform.
     * 
     * @return An instance of <code>CordeliaPlatform</code>.
     */
    public static CordeliaPlatform createPlatform(final Mode mode,
            final Environment environment) {
        Assert.assertIsNull("Platform has already been created.", PLATFORM);
        PLATFORM = new CordeliaPlatform();
        return PLATFORM;
    }

    /**
     * Obtain an instance of the platform.
     * 
     * @return An instance of <code>CordeliaPlatform</code>.
     */
    public static CordeliaPlatform getPlatform() {
        Assert.assertNotNull(PLATFORM, "Platform has not been created.");
        return PLATFORM;
    }

    /** A cordelia <code>ApplicationFactory</code>. */
    private final ApplicationFactory applicationFactory;

    /**
     * Create CordeliaPlatform.
     * 
     */
    private CordeliaPlatform() {
        super();
        this.applicationFactory = new ApplicationFactory(this);
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.Platform#end()
     * 
     */
    public void end() {
        Assert.assertNotYetImplemented("End is not yet implemented.");
    }

    /* (non-Javadoc)
     * @see com.thinkparity.codebase.ui.platform.Platform#hibernate(com.thinkparity.codebase.ApplicationId)
     */
    public void hibernate(final ApplicationId applicationId) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see com.thinkparity.codebase.ui.application.ApplicationListener#notifyEnd(com.thinkparity.codebase.ui.application.Application)
     * 
     */
    public void notifyEnd(final Application application) {
        Assert.assertNotYetImplemented("Notify end is not yet implemented.");
    }

    /**
     * @see com.thinkparity.codebase.ui.application.ApplicationListener#notifyHibernate(com.thinkparity.codebase.ui.application.Application)
     * 
     */
    public void notifyHibernate(final Application application) {
        Assert.assertNotYetImplemented("Notify hibernate is not yet implemented.");
    }

    /**
     * @see com.thinkparity.codebase.ui.application.ApplicationListener#notifyRestore(com.thinkparity.codebase.ui.application.Application)
     * 
     */
    public void notifyRestore(final Application application) {
        Assert.assertNotYetImplemented("Notify restore is not yet implemented.");
    }

    /**
     * @see com.thinkparity.codebase.ui.application.ApplicationListener#notifyStart(com.thinkparity.codebase.ui.application.Application)
     * 
     */
    public void notifyStart(final Application application) {
        Assert.assertNotYetImplemented("Notify start is not yet implemented.");
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.Platform#restart()
     * 
     */
    public void restart() {
        Assert.assertNotYetImplemented("Restart is not yet implemented.");
    }

    /* (non-Javadoc)
     * @see com.thinkparity.codebase.ui.platform.Platform#restore(com.thinkparity.codebase.ApplicationId)
     */
    public void restore(final ApplicationId applicationId) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see com.thinkparity.codebase.ui.platform.Platform#start()
     * 
     */
    public void start() {
        notifyLifeCycleStarting();
        logger.logApiId();
        startPlugins();
        startApplications();
        notifyLifeCycleStarted();
    }

    private void startApplications() {
        applicationFactory.create(Id.ADMIN).start(this);
    }

    private void startPlugins() {}
}
