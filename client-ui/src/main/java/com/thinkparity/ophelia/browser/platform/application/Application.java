/*
 * Feb 4, 2006
 */
package com.thinkparity.ophelia.browser.platform.application;

import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;

/**
 * <b>Title:</b>thinkParity OpheliaUI Application<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public interface Application {

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
     * Set the thinkParity user's profile.
     * 
     * @param profile
     *            A thinkParity user's profile.
     */
    public void setProfile(final Profile profile);

    /**
	 * Start the application.
	 *
     * @param platform
     *      The platform.
	 */
	public void start(final Platform platform);
}
