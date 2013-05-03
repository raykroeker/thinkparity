/**
 * 
 */
package com.thinkparity.codebase.ui.application;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.ui.platform.Platform;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ApplicationFactory<T extends Platform> {

    /** An application registry. */
    private final ApplicationRegistry<T> registry;

    /**
     * Create ApplicationFactory.
     * 
     * @param platform
     *            An instance of the thinkParity platform.
     * @param registry
     *            A <code>T</code> <code>ApplicationRegistry</code>.
     */
    protected ApplicationFactory(final ApplicationRegistry<T> registry) {
        super();
        this.registry = registry;
    }

    /**
     * Create an application.
     * 
     * @param id
     *            An <code>ApplicationId</code>.
     * @return An <code>Application</code>.
     */
    public final Application<T> create(final ApplicationId id) {
        if (registry.contains(id)) {
            return registry.get(id);
        } else {
            final Application<T> application = doCreate(id);
            register(application);
            return application;
        }
    }

    /**
     * Create the application specified by the id.
     * 
     * @param id
     *            The application id.
     * @return The application.
     */
    protected abstract Application<T> doCreate(final ApplicationId id);

    /**
     * Register an application.
     * 
     * @param application
     *            An <code>Application</code>.
     */
    private void register(final Application<T> application) {
        Assert.assertNotTrue(
                "APPLICATION ALREADY REGISTERED",
                registry.contains(application.getId()));
        registry.put(application);
    }
}
