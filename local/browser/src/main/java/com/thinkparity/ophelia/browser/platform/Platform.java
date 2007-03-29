/*
 * Mar 16, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.workspace.Workspace;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationListener;
import com.thinkparity.ophelia.browser.platform.application.window.WindowRegistry;
import com.thinkparity.ophelia.browser.platform.event.LifeCycleListener;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.util.ModelFactory;

import org.apache.log4j.Logger;

/**
 * <b>Title:</b>thinkParity OpheliaUI Platform<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface Platform extends ApplicationListener {

    /**
     * Add a platform life cycle listener.
     * 
     * @param listener
     *            A <code>LifeCycleListener</code>.
     */
    public void addListener(final LifeCycleListener listener);

    /**
     * Create a temporary file.
     * 
     * @param suffix
     *            The suffix string to be used in generating the file's name.
     * @return A <code>File</code>.
     * @throws IOException
     */
    public File createTempFile(final String suffix) throws IOException;

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

	/**
     * Obtain the release for the platform.
     * 
     * @return The platform <code>Release</code>.
     */
    public Release getRelease();

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
     * Initialize a workspace.
     * 
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @param loginMonitor
     *            A <code>LoginMonitor</code>.
     * @param credentials
     *            A user's <code>Credentials</code>.
     */
    public void initializeWorkspace(final ProcessMonitor monitor,
            final Workspace workspace, final Credentials credentials)
            throws InvalidCredentialsException;

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
     * Determine if sign-up is available.
     * 
     * @return True if sign-up is available for the user.
     */
    @Deprecated
    public Boolean isSignUpAvailable();

    /**
	 * Indicates whether or not the platform is running in test mode.
	 * 
	 * @return True if the application is in debug mode; false otherwise.
	 * 
	 * @see #isDevelopmentMode()
	 */
	public Boolean isTestingMode();

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
    public void removeListener(final LifeCycleListener listener);

    /**
     * Restart the platform.
     * 
     */
    public void restart();

    /**
	 * RestoreBrower an application from hibernation.
	 * 
	 * @param applicationId
	 *            The application id.
	 */
	public void restore(final ApplicationId applicationId);

    /**
     * Run the login action.
     * 
     * @param username
     *            The username <code>String</code>.
     * @param password
     *            The password <code>String</code>.
     * @param monitor
     *            A <code>ThinkParitySwingMonitor</code>.
     */
    public void runLogin(final String username, final String password,
            final ThinkParitySwingMonitor monitor);

    /**
     * Run the reset password action.
     * 
     */
    public void runResetPassword();

    /**
     * Start the browser platform.
     *
     */
    public void start();

    /** Connection status. */
    public enum Connection { OFFLINE, ONLINE }
}
