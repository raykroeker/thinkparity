/*
 * Created On: Fri Jun 02 2006 15:50 PDT
 */
package com.thinkparity.ophelia.browser.environment;

import com.thinkparity.codebase.assertion.Assert;
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
        final String environmentProperty =
            System.getProperty("thinkparity.environment");
        final Environment environment = Environment.valueOf(environmentProperty);
        switch (environment) {
        case DEMO:
        case DEMO_LOCALHOST:
            new ScenarioManager().select().execute();
            break;
        case DEVELOPMENT_LOCALHOST:
        case DEVELOPMENT_RAYMOND:
        case DEVELOPMENT_ROBERT:
        case PRODUCTION:
        case TESTING:
        case TESTING_LOCALHOST:
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN ENVIRONMENT");
        }
        return environment;
    }
}
