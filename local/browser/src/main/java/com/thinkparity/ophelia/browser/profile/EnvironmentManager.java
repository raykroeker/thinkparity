/*
 * Created On: Fri Jun 02 2006 15:50 PDT
 */
package com.thinkparity.ophelia.browser.profile;

import com.thinkparity.codebase.model.session.Environment;

public final class EnvironmentManager {

    /** Create EnvironmentManager. */
    public EnvironmentManager() { super(); }

    /**
     * Select an environment.
     *
     * @return A <code>Environment</code>.
     */
    public Environment select() {
        final String thinkParityEnvironment = System.getProperty("thinkparity.environment");
        return Environment.valueOf(thinkParityEnvironment);
    }
}
