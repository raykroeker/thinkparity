/*
 * Mar 16, 2006
 */
package com.thinkparity.browser.platform;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationListener;
import com.thinkparity.browser.platform.application.window.WindowRegistry;

import com.thinkparity.model.parity.model.workspace.Preferences;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Platform extends ApplicationListener {

    /**
     * End the platform.
     *
     */
    public void end();

    public AvatarRegistry getAvatarRegistry();

    public Logger getLogger(final Class clasz);

	public ModelFactory getModelFactory();
	public BrowserPlatformPersistence getPersistence();
	public Preferences getPreferences();
	public WindowRegistry getWindowRegistry();
	/**
	 * Request that the application hibernate.
	 * 
	 * @param applicationId
	 *            The application id.
	 */
	public void hibernate(final ApplicationId applicationId);

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

	/** Restart the platform. */
    public void restart();

    /**
     * Restart the platform with the specified properties.
     * 
     * @param properties
     *            Platform properties.
     */
    public void restart(final Properties properties);

    /**
	 * RestoreBrower an application from hibernation.
	 * 
	 * @param applicationId
	 *            The application id.
	 */
	public void restore(final ApplicationId applicationId);

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

    /** Connection status. */
    public enum Connection { OFFLINE, ONLINE }
}
