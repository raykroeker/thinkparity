/*
 * Mar 16, 2006
 */
package com.thinkparity.codebase.ui.application;

import com.thinkparity.codebase.ui.platform.Platform;

/**
 * A queryable interface into the application registry. Works with
 * link{#ApplicationFactory} to maintain a single point of reference for
 * platform applications.
 * 
 * @see Application
 * @see ApplicationFactory#create(ApplicationId)
 * @see ApplicationId
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class ApplicationRegistry<T extends Platform> {

	/**
     * Create ApplicationRegistry.
     * 
     * @param platform
     *            A <code>Platform</code>.
     */
	protected ApplicationRegistry() {
        super();
	}

	/**
     * Query whether or not the registry contains the application.
     * 
     * @param id
     *            An application id.
     * @return The application.
     */
    public abstract boolean contains(final ApplicationId id);

	/**
	 * Obtain a registered application.
	 * 
	 * @param id
	 *            The application id.
	 * @return The application; if it has been registered; null otherwise.
	 */
    public abstract Application<T> get(final ApplicationId id);

	/**
	 * Obtain the current status of the application.
	 * 
     * @param id
     *            An application id.
	 * @return The application status.
	 */
    public abstract ApplicationStatus getStatus(final ApplicationId id);

	/**
     * Remove an application from the registry.
     * 
     * @param id
     *            An application id.
     */
	public abstract Application<T> remove(final ApplicationId id);

	/**
	 * Regiser an application.
	 * 
	 * @param application
	 *            The application.
	 * @return The previously registered instance.
	 */
	protected abstract Application<T> put(final Application<T> application);
}
