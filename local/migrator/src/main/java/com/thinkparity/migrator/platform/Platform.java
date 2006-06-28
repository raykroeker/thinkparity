/*
 * Created On: Sun Jun 25 2006 10:46 PDT
 * $Id$
 */
package com.thinkparity.migrator.platform;

import java.util.Properties;

/**
 * <b>Title:</b>thinkParity Application Platform<br>
 * <b>Description:</b>The migrator local client platform interface.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public interface Platform {

    /**
     * End the platform.
     *
     */
    public void end();

	/**
	 * Indicates whether or not the platform is running in debug mode.
	 * 
	 * @return True if the application is in debug mode; false otherwise.
	 * 
	 * @see #isTestingMode()
	 */
	public Boolean isDevelopmentMode();

    /**
     * Determine if the user has online capability.
     * 
     * @return True if the user has online capability.
     */
    public Boolean isOnline();

    /**
	 * Indicates whether or not the platform is running in test mode.
	 * 
	 * @return True if the application is in debug mode; false otherwise.
	 * 
	 * @see #isDevelopmentMode()
	 */
	public Boolean isTestingMode();

	/**
     * Restart the platform.
     *
     */
    public void restart();

    /**
     * Restart the platform with the specified properties.
     * 
     * @param properties
     *            Platform properties.
     */
    public void restart(final Properties properties);

    /**
     * Start the browser platform.
     *
     */
    public void start();

    /**
     * Update the browser platform.
     *
     */
    public void update();
}
