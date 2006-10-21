/**
 * 
 */
package com.thinkparity.codebase.ui.avatar;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.ui.application.Application;
import com.thinkparity.codebase.ui.platform.Platform;
import com.thinkparity.codebase.ui.provider.Provider;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AvatarFactory<T extends Platform, U extends Application<T>> {

    /** An avatar registry. */
    private final AvatarRegistry<T, U> registry;

    /**
     * Create ApplicationFactory.
     * 
     * @param platform
     *            An instance of the thinkParity platform.
     * @param registry
     *            A <code>T</code> <code>ApplicationRegistry</code>.
     */
    protected AvatarFactory(final AvatarRegistry<T, U> registry) {
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
    public final Avatar<T, U, ? extends Provider> create(final AvatarId id) {
        if (registry.contains(id)) {
            return registry.get(id);
        } else {
            final Avatar<T, U, ? extends Provider> avatar = doCreate(id);
            register(avatar);
            return avatar;
        }
    }

    /**
     * Create the application specified by the id.
     * 
     * @param id
     *            The application id.
     * @return The application.
     */
    protected abstract Avatar<T, U, ? extends Provider> doCreate(
            final AvatarId id);

    /**
     * Register an application.
     * 
     * @param application
     *            An <code>Application</code>.
     */
    private <V extends Provider> void register(final Avatar<T, U, V> avatar) {
        Assert.assertNotTrue(
                "APPLICATION ALREADY REGISTERED",
                registry.contains(avatar.getId()));
        registry.put(avatar);
    }
}
