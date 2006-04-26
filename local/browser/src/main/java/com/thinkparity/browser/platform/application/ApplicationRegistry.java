/*
 * Mar 16, 2006
 */
package com.thinkparity.browser.platform.application;

import java.util.HashMap;
import java.util.Map;

/**
 * A queryable interface into the application registry. Works with
 * link{#ApplicationFactory} to maintain a single point of reference for
 * platform applications.
 * 
 * @see Application
 * @see ApplicationFactory#create(ApplicationId)
 * @see ApplicationId
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ApplicationRegistry {

	/** An application registry. */
	private static final Map<ApplicationId, Object> REGISTRY;

	static { REGISTRY = new HashMap<ApplicationId, Object>(2, 1.0F); }

	/** Create an ApplicationRegistry. */
	public ApplicationRegistry() { super(); }

	/**
     * Query whether or not the registry contains the application.
     * 
     * @param id
     *            An application id.
     * @return The application.
     */
	public Boolean contains(final ApplicationId id) {
		synchronized(REGISTRY) { return REGISTRY.containsKey(id); }
	}

	/**
	 * Obtain a registered application.
	 * 
	 * @param id
	 *            The application id.
	 * @return The application; if it has been registered; null otherwise.
	 */
	public Application get(final ApplicationId id) {
		synchronized(REGISTRY) { return (Application) REGISTRY.get(id); }
	}

	/**
	 * Obtain the current status of the application.
	 * 
     * @param id
     *            An application id.
	 * @return The application status.
	 */
	public ApplicationStatus getStatus(final ApplicationId id) {
		synchronized(REGISTRY) {
			return ((Application) REGISTRY.get(id)).getStatus();
		}
	}

	/**
     * Remove an application from the registry.
     * 
     * @param id
     *            An application id.
     */
	public void remove(final ApplicationId id) {
		synchronized(REGISTRY) { REGISTRY.remove(id); }
	}

	/**
	 * Regiser an application.
	 * 
	 * @param application
	 *            The application.
	 * @return The previously registered instance.
	 */
	void put(final Application application) {
		synchronized(REGISTRY) { REGISTRY.put(application.getId(), application); }
	}
}
