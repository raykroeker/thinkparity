/*
 * Created On: Fri Jun 02 2006 15:50 PDT
 */
package com.thinkparity.ophelia.browser.profile;

import com.thinkparity.codebase.model.session.Environment;

public final class EnvironmentManager {

    /** Create EnvironmentManager. */
    public EnvironmentManager() { super(); }

    /**
     * Prompt the user to select a profile.
     *
     * @return A profile.
     */
    public Environment select() {
        final Environment environment;
        final String thinkParityEnvironment = System.getProperty("thinkparity.environment");
        if ("development.localhost".equals(thinkParityEnvironment)) {
            environment = Environment.DEVELOPMENT_LOCALHOST;
        } else if ("development.raymond".equals(thinkParityEnvironment)) {
            environment = Environment.DEVELOPMENT_RAYMOND;
        } else if ("development.robert".equals(thinkParityEnvironment)) {
            environment = Environment.DEVELOPMENT_ROBERT;
        } else if ("production".equals(thinkParityEnvironment)) {
            environment = Environment.PRODUCTION;
        } else if ("testing".equals(thinkParityEnvironment)) {
            environment = Environment.TESTING;
        } else if ("testing.localhost".equals(thinkParityEnvironment)) {
            environment = Environment.TESTING_LOCALHOST;
        } else {
            environment = Environment.PRODUCTION;
        }
        return environment;
    }
}
