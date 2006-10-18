/*
 * Created On: Mon 0ct 16, 2006 10:14 
 */
package com.thinkparity.ophelia.browser.mode;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.ophelia.browser.mode.demo.Scenario;
import com.thinkparity.ophelia.browser.mode.demo.DemoManager;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ModeManager {

    /** Create ModeManager. */
    public ModeManager() {
        super();
    }

    /**
     * Select a thinkParity mode. Any custom input required by a given mode will
     * be performed here as well; for example in the case of a demo mode;
     * initializing the local demo profiles; and running the scenarios will be
     * done here.
     * 
     * @return A thinkParity <code>Mode</code>.
     */
    public Mode select() {
        final String modeProperty = System.getProperty("thinkparity.mode");
        final Mode mode = Mode.valueOf(modeProperty);
        switch (mode) {
        case DEMO:
            final Scenario scenario = new DemoManager().select();
            if (null != scenario) {
                scenario.execute();
            }
            break;
        case DEVELOPMENT:
        case PRODUCTION:
        case TESTING:
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN MODE");
        }
        return mode;
    }
}
