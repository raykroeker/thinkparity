/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.platform;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationFactory;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;
import com.thinkparity.browser.platform.application.window.WindowRegistry;
import com.thinkparity.browser.platform.login.LoginHelper;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser2Platform implements Platform {

	/**
	 * The singleton instance of the platform.
	 * 
	 */
	private static Browser2Platform b2Platform;

	static { Browser2PlatformInitializer.initialize(); }

	/**
	 * Start the B2 platform.
	 * 
	 * @param args
	 *            Startup arguments.
	 */
	public static void start(final String[] args) {
		Assert.assertIsNull(
				"Cannot start the platform more than once.",
				b2Platform);
		b2Platform = new Browser2Platform();
		b2Platform.doStart();
	}

	/**
	 * The platform's logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * The application registry.
	 * 
	 */
	private final ApplicationRegistry applicationRegistry;

	/**
	 * The avatar registry.
	 * 
	 */
	private final AvatarRegistry avatarRegistry;

	/**
	 * The platform's login helper.
	 * 
	 */
	private final LoginHelper loginHelper;

	/**
	 * The platform's mode of operation.
	 * 
	 */
	private final Mode mode;

	/**
	 * The parity model factory.
	 * 
	 */
	private final ModelFactory modelFactory;

	/**
	 * The platform settings.
	 * 
	 */
	private final Browser2PlatformPersistence persistence;

	/**
	 * The parity preferences.
	 * 
	 */
	private final Preferences preferences;

	/**
	 * The window registry.
	 * 
	 */
	private final WindowRegistry windowRegistry;

	/**
	 * The parity workspace.
	 * 
	 */
	private final Workspace workspace;

	/**
	 * Create a Browser2Platform [Singleton]
	 * 
	 */
	private Browser2Platform() {
		this.applicationRegistry = new ApplicationRegistry();
		this.avatarRegistry = new AvatarRegistry();
		this.windowRegistry = new WindowRegistry();
		this.mode = Mode.DEBUG;
		this.modelFactory = ModelFactory.getInstance();
		this.preferences = modelFactory.getPreferences(getClass());
		this.workspace = modelFactory.getWorkspace(getClass());

		this.logger = LoggerFactory.getLogger(getClass());
		this.loginHelper = new LoginHelper(this);
		this.persistence = new Browser2PlatformPersistence(this);
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#getAvatarRegistry()
	 * 
	 */
	public AvatarRegistry getAvatarRegistry() { return avatarRegistry; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#getLogger(java.lang.Class)
	 * 
	 */
	public Logger getLogger(final Class clasz) {
		return LoggerFactory.getLogger(clasz);
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#getModelFactory()
	 * 
	 */
	public ModelFactory getModelFactory() { return modelFactory; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#getPersistence()
	 * 
	 */
	public Browser2PlatformPersistence getPersistence() {
		return persistence;
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#getPreferences()
	 * 
	 */
	public Preferences getPreferences() { return preferences; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#getWindowRegistry()
	 * 
	 */
	public WindowRegistry getWindowRegistry() { return windowRegistry; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#hibernate(com.thinkparity.browser.platform.application.ApplicationId)
	 * 
	 */
	public void hibernate(final ApplicationId applicationId) {
		applicationRegistry.get(applicationId).hibernate(this);
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#isDebugMode()
	 * 
	 */
	public Boolean isDebugMode() { return mode == Mode.DEBUG; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#isTestMode()
	 */
	public Boolean isTestMode() {
		if(isDebugMode()) { return Boolean.TRUE; }
		return mode == Mode.TEST;
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyEnd(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyEnd(final Application application) {
		logger.info("[BROWSER2] [PLATFORM] [APPLICATION ENDED]");
		Assert.assertTrue(
				"Application not found:  " + application.getId(),
				applicationRegistry.contains(application.getId()));

		applicationRegistry.erase(application.getId());
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyHibernate(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyHibernate(final Application application) {
		logger.info("[BROWSER2] [PLATFORM] [APPLICATION HIBERNATING]");
		if(ApplicationId.BROWSER2 == application.getId()) {
			restore(ApplicationId.SYS_APP);
		}
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyRestore(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyRestore(final Application application) {
		logger.info("[BROWSER2] [PLATFORM] [APPLICATION RESTORED]");
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyStart(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyStart(final Application application) {
		logger.info("[BROWSER2] [PLATFORM] [APPLICATION STARTED]");
		if(ApplicationId.SYS_APP == application.getId()) {
			hibernate(application.getId());
		}
	}

	/**
	 * @see com.thinkparity.browser.platform.Platform#restore(com.thinkparity.browser.platform.application.ApplicationId)
	 * 
	 */
	public void restore(final ApplicationId applicationId) {
		logger.info("[BROWSER2] [PLATFORM] [RESTORING APPLICATION]");
		applicationRegistry.get(applicationId).restore(this);
	}

	/**
	 * Start the browser platform.
	 *
	 */
	private void doStart() {
		login();
		if(isLoggedIn()) {
			startApplication(ApplicationId.SYS_APP);
			startApplication(ApplicationId.BROWSER2);
		}
	}

	/**
	 * End the application.
	 * 
	 * @param id
	 *            The application id.
	 */
	private void endApplication(final ApplicationId id) {
		applicationRegistry.get(id).end(this);
	}

	/**
	 * End all applications.
	 *
	 */
	private void endApplications() {
		for(final ApplicationId id : ApplicationId.values()) {
			if(applicationRegistry.contains(id)) { endApplication(id); }
		}
	}

	/**
	 * Check the registry for the application; and if it does not yet exist;
	 * create it.
	 * 
	 * @param id
	 *            The application id.
	 * @return The pplication.
	 */
	private Application getApplication(final ApplicationId id) {
		if(applicationRegistry.contains(id)) { return applicationRegistry.get(id); }
		else { return ApplicationFactory.create(this, id); }
	}

	/**
	 * Determine whether or not the user is logged in.
	 * 
	 * @return True if the user is logged in false otherwise.
	 */
	private Boolean isLoggedIn() { return loginHelper.isLoggedIn(); }

	/**
	 * Check if the user has set auto-login. If so; attempt an auto-login;
	 * otherwise attempt a manual login until the user cancels.
	 * 
	 */
	private void login() { loginHelper.login(); }

	/**
	 * Get the application an start it.
	 * 
	 * @param applicationId
	 *            The application id.
	 */
	private void startApplication(final ApplicationId id) {
		getApplication(id).start(this);
	}

	/**
	 * Discrete modes the platform is capable of running in.
	 * 
	 */
	private enum Mode { DEBUG, PRODUCTION, TEST }
}
