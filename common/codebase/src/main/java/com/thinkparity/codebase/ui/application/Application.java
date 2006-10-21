/*
 * Feb 4, 2006
 */
package com.thinkparity.codebase.ui.application;

import com.thinkparity.codebase.ui.platform.Platform;
import com.thinkparity.codebase.ui.platform.Saveable;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Application<T extends Platform> extends Saveable {

	/**
	 * Add a listener to the application. The listener is used to generate
	 * status change events.
	 * 
	 * @param l
     *      The application listener.
	 */
	public boolean addListener(final ApplicationListener listener);

	/**
	 * End the application.
	 *
     * @param platform
     *      The platform.
	 */
	public void end(final T platform);

    /**
     * Obtain the application's unique id.
     * 
     * @return An <code>ApplicationId</code>.
     */
    public ApplicationId getId();

    /**
	 * Determine the application's current status.
	 * 
	 * @return The application's current status.
	 */
	public ApplicationStatus getStatus();

	/**
	 * Hibernate the application.
	 *
     * @param platform
     *      The platform.
	 */
	public void hibernate(final T platform);

    /**
     * Add a listener to the application. The listener is used to generate
     * status change events.
     * 
     * @param l
     *            The application listener.
     */
	public boolean removeListener(final ApplicationListener listener);

	/**
     * Restore the application from hibernation.
     * 
     * @param platform
     *            The platform.
     */
	public void restore(final T platform);

    /**
	 * Start the application.
	 *
     * @param platform
     *      The platform.
	 */
	public void start(final T platform);
}
