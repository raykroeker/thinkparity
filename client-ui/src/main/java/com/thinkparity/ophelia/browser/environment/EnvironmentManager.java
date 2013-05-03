/*
 * Created On: Fri Jun 02 2006 15:50 PDT
 */
package com.thinkparity.ophelia.browser.environment;

import com.thinkparity.codebase.model.session.Environment;

/**
 * <b>Title:</b>thinkParity Ophelia UI Environment Manager<br>
 * <b>Description:</b>Used to select the environment to interact with.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class EnvironmentManager {

    /**
     * Create EnvironmentManager.
     * 
     * @param internal
     *            A <code>Boolean</code>.
     */
    public EnvironmentManager(final Boolean internal) {
        super();
    }

    /**
     * Select an environment.
     *
     * @return A <code>Environment</code>.
     */
    public Environment select() {
        final String environmentProperty =
            System.getProperty("thinkparity.environment");
        return Environment.valueOf(environmentProperty);
    }
}
