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
	boolean addListener(final ApplicationListener<T> listener);

	/**
	 * End the application.
	 *
     * @param platform
     *      The platform.
	 */
	void end(final T platform);

    /**
     * Obtain the application's unique id.
     * 
     * @return An <code>ApplicationId</code>.
     */
    ApplicationId getId();

    /**
	 * Determine the application's current status.
	 * 
	 * @return The application's current status.
	 */
	ApplicationStatus getStatus();

	/**
	 * Hibernate the application.
	 *
     * @param platform
     *      The platform.
	 */
	void hibernate(final T platform);

    /**
     * Add a listener to the application. The listener is used to generate
     * status change events.
     * 
     * @param l
     *            The application listener.
     */
	boolean removeListener(final ApplicationListener<T> listener);

	/**
     * Restore the application from hibernation.
     * 
     * @param platform
     *            The platform.
     */
	void restore(final T platform);

    /**
	 * Start the application.
	 *
     * @param platform
     *      The platform.
	 */
	void start(final T platform);
}
