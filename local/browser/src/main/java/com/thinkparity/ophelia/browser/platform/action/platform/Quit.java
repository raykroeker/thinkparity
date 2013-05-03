/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.platform;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Quit extends AbstractAction {

    /** The browser platform. */
    private final Platform platform;

    /**
     * Create a QuitApplication.
     * 
     * @param platform
     *            The browser platform.
     */
    public Quit(final Platform platform) {
        super(ActionId.PLATFORM_QUIT);
        this.platform = platform;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        logger.logInfo("Begin quit.");
        try {
            platform.end();
        } catch (final Throwable t) {
            logger.logFatal(t, "Could not end platform.");
            /* TODO - Quit#invoke(Data) - generalize platform termination */
            System.exit(1);
        }
        if (Boolean.FALSE == platform.isDevelopmentMode() &&
                Boolean.FALSE == platform.isTestingMode()) {
            logger.logInfo("End quit (terminate).");
            System.exit(1);
        }
        logger.logInfo("End quit.");
    }
}
