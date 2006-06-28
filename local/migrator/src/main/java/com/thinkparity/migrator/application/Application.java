/*
 * Created On: Sun Jun 25 2006 10:53 PDT
 * $Id$
 */
package com.thinkparity.migrator.application;

import com.thinkparity.migrator.platform.Platform;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
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
     * Obtain the application id.
     * 
     * @return The application id.
     */
    public ApplicationId getId();

	/**
	 * Hibernate the application.
	 *
     * @param platform
     *      The platform.
	 */
	public void hibernate(final Platform platform);

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
