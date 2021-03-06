/*
 * Mar 16, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;

import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.workspace.InitializeMediator;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.configuration.Configuration;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.platform.LearnMore;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationListener;
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
     * Display an error dialog for an application.
     * 
     * @param applicationId
     *            An <code>ApplciationId</code>.
     * @param error
     *            An error <code>Throwable</code>.
     */
    public void displayErrorDialog(final ApplicationId applicationId,
            final Throwable error);

    /**
     * Display an error dialog for an application.
     * 
     * @param applicationId
     *            An <code>ApplciationId</code>.
     * @param error
     *            An error <code>Throwable</code>.
     * @param errorMessageKey
     *            An error message localization key <code>String</code>.
     * @param errorMessageArguments
     *            An error message localization argument <code>Object[]</code>.
     */
    public void displayErrorDialog(final ApplicationId applicationId,
            final Throwable error, final String errorMessageKey,
            final Object... errorMessageArguments);

    /**
     * Display an error dialog.
     * 
     * @param error
     *            An error <code>Throwable</code>.
     */
    public void displayErrorDialog(final Throwable error);

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
     * Obtain the build id.
     * 
     * @return A build id <code>String</code>.
     */
    public String getBuildId();

    /**
     * Obtain the configuration.
     * 
     * @return The <code>Configuration</code>.
     */
    public Configuration getConfiguration();

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

    /**
     * Obtain a logger.
     * 
     * @param clasz
     *            A <code>Class<?></code>.
     * @return A <code>Logger</code>.
     */
    public Logger getLogger(final Class<?> clasz);

    public ModelFactory getModelFactory();

    public BrowserPlatformPersistence getPersistence();

	/**
     * Obtain the plugin registry for the platform.
     * 
     * @return A plugin registry.
     */
    public PluginRegistry getPluginRegistry();

	/**
     * Obtain the release name.
     * 
     * @return The platform release name <code>String</code>.
     */
    public String getReleaseName();

    /**
     * Obtain the time zone.
     * 
     * @return The <code>TimeZone</code>.
     */
    public TimeZone getTimeZone();

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
     * @param loginMonitor
     *            A <code>LoginMonitor</code>.
     * @param mediator
     *            A <code>InitializeMediator</code>.        
     * @param workspace
     *            A thinkParity <code>Workspace</code>.
     * @param credentials
     *            A user's <code>Credentials</code>.
     */
    public void initializeWorkspace(final ProcessMonitor monitor,
            final InitializeMediator mediator,
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
     * Determine if we are running internal.
     * 
     * @return True if we are internal.
     */
    public Boolean isInternal();

    /**
     * Determine whether or not the platform is online.
     * 
     * @return True if the platform is online.
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
     * Run the contact us action.
     */
    public void runContactUs();

    /**
     * Run the learn more action.
     * 
     * @param topic
     *            The action <code>LearnMore.Topic</code>.
     */
    public void runLearnMore(final LearnMore.Topic topic);

    /**
     * Run the login action.
     * 
     * @param credentials
     *            The <code>Credentials</code>.
     * @param monitor
     *            A <code>ThinkParitySwingMonitor</code>.
     * @param mediator
     *            A <code>InitializeMediator</code>.   
     */
    public void runLogin(final Credentials credentials,
            final ThinkParitySwingMonitor monitor,
            final InitializeMediator mediator);

    /**
     * Run the show getting started movie action.
     */
    public void runShowGettingStartedMovie();

    /**
     * Start the browser platform.
     *
     */
    public void start();

    /** Connection status. */
    public enum Connection { OFFLINE, ONLINE }
}
