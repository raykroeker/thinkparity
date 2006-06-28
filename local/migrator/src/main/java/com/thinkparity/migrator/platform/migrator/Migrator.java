/*
 * Created On: Sun Jun 25 2006 10:49 PDT
 * $Id$
 */
package com.thinkparity.migrator.platform.migrator;

import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.migrator.application.ApplicationFactory;
import com.thinkparity.migrator.application.ApplicationId;
import com.thinkparity.migrator.platform.Platform;

/**
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Migrator implements Platform {

    public Migrator() { super(); }

    /**
     * @see com.thinkparity.migrator.platform.Platform#end()
     */
    public void end() {
        throw Assert.createNotYetImplemented("Application#end");
    }

    /**
     * @see com.thinkparity.migrator.platform.Platform#isDevelopmentMode()
     */
    public Boolean isDevelopmentMode() {
        throw Assert.createNotYetImplemented("Application#isDevelopmentMode");
    }

    /**
     * @see com.thinkparity.migrator.platform.Platform#isOnline()
     */
    public Boolean isOnline() {
        throw Assert.createNotYetImplemented("Application#isOnline");
    }

    /**
     * @see com.thinkparity.migrator.platform.Platform#isTestingMode()
     */
    public Boolean isTestingMode() {
        throw Assert.createNotYetImplemented("Application#isTestingMode");
    }

    /**
     * @see com.thinkparity.migrator.platform.Platform#restart()
     */
    public void restart() {
        throw Assert.createNotYetImplemented("Application#restart");
    }

    /**
     * @see com.thinkparity.migrator.platform.Platform#restart(java.util.Properties)
     */
    public void restart(Properties properties) {
        throw Assert.createNotYetImplemented("Application#restart");
    }

    /**
     * @see com.thinkparity.migrator.platform.Platform#start()
     */
    public void start() {
        ApplicationFactory.create(this, ApplicationId.MIGRATOR).start(this);
    }

    /**
     * @see com.thinkparity.migrator.platform.Platform#update()
     */
    public void update() {
        throw Assert.createNotYetImplemented("Application#update");
    }
}
