/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.platform.application;

import org.apache.log4j.Logger;

import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.Saveable;
import com.thinkparity.browser.platform.Platform.Connection;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Application extends Saveable {

	/**
	 * Add a listener to the application. The listener is used to generate
	 * status change events.
	 * 
	 * @param l
     *      The application listener.
	 */
	public void addListener(final ApplicationListener l);

	/**
	 * End the application.
	 *
     * @param platform
     *      The platform.
	 */
	public void end(final Platform platform);

    /**
     * Obtain the application connection info.
     * 
     * @return The connection info.
     */
    public Connection getConnection();

	/**
	 * Obtain the application id.
	 * 
	 * @return The application id.
	 */
	public ApplicationId getId();

    /**
     * Obtain a logger for the class from the applilcation.
     *
     * @param clasz
     *      The class for which to obtain the logger.
     * @return An apache logger.
     */
    public Logger getLogger(final Class clasz);

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
	public void hibernate(final Platform platform);

    /**
     * Determine whether or not the application is in development mode.
     * 
     * @return True if the application is in development mode.
     */
	public Boolean isDevelopmentMode();

	/**
	 * Add a listener to the application. The listener is used to generate
	 * status change events.
	 * 
	 * @param l
	 *            The application listener.
	 */
	public void removeListener(final ApplicationListener l);

	/**
	 * Restore the application from hibernation.
	 *
     * @param platform
     *      The platform.
	 */
	public void restore(final Platform platform);

    /**
	 * Start the application.
	 *
     * @param platform
     *      The platform.
	 */
	public void start(final Platform platform);
}
