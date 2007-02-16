/*
 * Mar 16, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.ophelia.model.session.LoginMonitor;
import com.thinkparity.ophelia.model.workspace.Preferences;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationListener;
import com.thinkparity.ophelia.browser.platform.application.window.WindowRegistry;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleListener;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.util.ModelFactory;

import org.apache.log4j.Logger;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Platform extends ApplicationListener {

    /**
     * Initialize a workspace.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @param loginMonitor
     *            A <code>LoginMonitor</code>.
     * @param credentials
     *            A user's <code>Credentials</code>.
     */
    public void initializeWorkspace(final Workspace workspace,
            final LoginMonitor loginMonitor, final Credentials credentials);

    /**
     * Determine if a workspace has been initialized.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>
     * @return True if the workspace has already been initialized.
     */
    public Boolean isWorkspaceInitialized(final Workspace workspace);

    /**
     * Add a platform life cycle listener.
     * 
     * @param listener
     *            A <code>LifeCycleListener</code>.
     */
    public void addListener(final LifeCycleListener listener);

    /**
     * End the platform.
     *
     */
    public void end();

    /**
     * Obtain all available locales.
     * 
     * @return The available <code>Locale[]</code>.
     */
    public Locale[] getAvailableLocales();

    /**
     * Obtain the available time zone.
     * 
     * @return The available <code>TimeZones[]</code>.
     */
    public TimeZone[] getAvailableTimeZones();

    public AvatarRegistry getAvatarRegistry();

    /**
     * Obtain the environment.
     * 
     * @return The <code>Environment</code>.
     */
    public Environment getEnvironment();

    /**
     * Obtain the locale.
     * 
     * @return The <code>Locale</code>.
     */
    public Locale getLocale();

    public Logger getLogger(final Class clasz);

	public ModelFactory getModelFactory();

	public BrowserPlatformPersistence getPersistence();

	/**
     * Obtain the plugin registry for the platform.
     * 
     * @return A plugin registry.
     */
    public PluginRegistry getPluginRegistry();

	public Preferences getPreferences();

    /**
     * Obtain the time zone.
     * 
     * @return The <code>TimeZone</code>.
     */
    public TimeZone getTimeZone();

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

	/**
     * Add a platform life cycle listener.
     * 
     * @param listener
     *            A <code>LifeCycleListener</code>.
     */
    public void removeListener(final LifeCycleListener listener);

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

    /** Connection status. */
    public enum Connection { OFFLINE, ONLINE }
}
