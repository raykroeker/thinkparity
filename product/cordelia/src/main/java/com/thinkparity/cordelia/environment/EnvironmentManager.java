/*
 * Created On: Fri Jun 02 2006 15:50 PDT
 */
package com.thinkparity.cordelia.environment;

import com.thinkparity.codebase.model.session.Environment;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class EnvironmentManager {

    /** Create EnvironmentManager. */
    public EnvironmentManager() { super(); }

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
