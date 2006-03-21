/*
 * Mar 16, 2006
 */
package com.thinkparity.browser.platform;

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
	public AvatarRegistry getAvatarRegistry();
	public Logger getLogger(final Class clasz);
	public ModelFactory getModelFactory();
	public Browser2PlatformPersistence getPersistence();
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
	 * @see #isTestMode()
	 */
	public Boolean isDebugMode();

	/**
	 * Indicates whether or not the platform is running in test mode.
	 * 
	 * @return True if the application is in debug mode; false otherwise.
	 * 
	 * @see #isDebugMode()
	 */
	public Boolean isTestMode();

	/**
	 * Restore an application from hibernation.
	 * 
	 * @param applicationId
	 *            The application id.
	 */
	public void restore(final ApplicationId applicationId);
}
