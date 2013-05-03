/*
 * Mar 16, 2006
 */
package com.thinkparity.codebase.ui.avatar;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.ui.application.Application;
import com.thinkparity.codebase.ui.platform.Platform;
import com.thinkparity.codebase.ui.provider.Provider;

/**
 * A queryable interface into the application registry. Works with
 * link{#ApplicationFactory} to maintain a single point of reference for
 * platform applications.
 * 
 * @see Avatar
 * @see AvatarFactory#create(AvatarId)
 * @see AvatarId
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AvatarRegistry<T extends Platform, U extends Application<T>> {

    /** The backing registry. */
    private static final Map<AvatarId, Object> REGISTRY;

    static {
        REGISTRY = new HashMap<AvatarId, Object>();
    }

	/**
     * Create ApplicationRegistry.
     * 
     * @param platform
     *            A <code>Platform</code>.
     */
	protected AvatarRegistry() {
        super();
	}

	/**
     * Query whether or not the registry contains the application.
     * 
     * @param id
     *            An application id.
     * @return The application.
     */
    public final boolean contains(final AvatarId id) {
        synchronized (REGISTRY) {
            return REGISTRY.containsKey(id);
        }
    }

	/**
	 * Obtain a registered application.
	 * 
	 * @param id
	 *            The application id.
	 * @return The application; if it has been registered; null otherwise.
	 */
    @SuppressWarnings("unchecked")
    public final <V extends Provider> Avatar<T, U, V> get(final AvatarId id) {
        synchronized (REGISTRY) {
            return (Avatar<T, U, V>) REGISTRY.get(id);
        }
    }

	/**
     * Remove an application from the registry.
     * 
     * @param id
     *            An application id.
     */
    @SuppressWarnings("unchecked")
	public final <V extends Provider> Avatar<T, U, V> remove(final AvatarId id) {
	    synchronized (REGISTRY) {
            return (Avatar<T, U, V>) REGISTRY.remove(id);
        }
    }

	/**
     * Regiser an application.
     * 
     * @param application
     *            The application.
     * @return The previously registered instance.
     */
    @SuppressWarnings("unchecked")
	protected final <V extends Provider> Avatar<T, U, V> put(final Avatar<T, U, V> application) {
	    synchronized (REGISTRY) {
	        return (Avatar<T, U, V>) REGISTRY.put(application.getId(), application);
        }
    }
}
