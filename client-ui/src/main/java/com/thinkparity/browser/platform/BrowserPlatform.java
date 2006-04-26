/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.platform;

import org.apache.log4j.Logger;

import com.thinkparity.browser.Version;
import com.thinkparity.browser.application.browser.display.avatar.AvatarRegistry;
import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationFactory;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;
import com.thinkparity.browser.platform.application.window.WindowRegistry;
import com.thinkparity.browser.platform.login.LoginHelper;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * 
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatform implements Platform {

	/**
	 * The singleton instance of the platform.
	 * 
	 */
	private static BrowserPlatform b2Platform;

	static { BrowserPlatformInitializer.initialize(); }

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
		b2Platform = new BrowserPlatform();
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
	 * The parity model factory.
	 * 
	 */
	private final ModelFactory modelFactory;

	/**
	 * The platform settings.
	 * 
	 */
	private final BrowserPlatformPersistence persistence;

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
	 * Create a BrowserPlatform [Singleton]
	 * 
	 */
	private BrowserPlatform() {
		this.applicationRegistry = new ApplicationRegistry();
		this.avatarRegistry = new AvatarRegistry();
		this.windowRegistry = new WindowRegistry();
		this.modelFactory = ModelFactory.getInstance();
		this.preferences = modelFactory.getPreferences(getClass());
		this.workspace = modelFactory.getWorkspace(getClass());

		this.logger = LoggerFactory.getLogger(getClass());
		this.loginHelper = new LoginHelper(this);
		this.persistence = new BrowserPlatformPersistence(this);
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
	public BrowserPlatformPersistence getPersistence() {
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
	public Boolean isDebugMode() { return Version.getMode() == Mode.DEVELOPMENT; }

	/**
	 * @see com.thinkparity.browser.platform.Platform#isTestMode()
	 */
	public Boolean isTestMode() {
		if(isDebugMode()) { return Boolean.TRUE; }
		return Version.getMode() == Mode.TESTING;
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyEnd(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyEnd(final Application application) {
        logger.info("[LBROWSER] [PLATFORM] [NOTIFY APPLICATION END]");
        logger.debug(application.getId());
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyHibernate(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyHibernate(final Application application) {
		logger.info("[LBROWSER] [PLATFORM] [NOTIFY APPLICATION HIBERNATE]");
        logger.debug(application.getId());
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyRestore(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyRestore(final Application application) {
        logger.info("[LBROWSER] [PLATFORM] [NOTIFY APPLICATION RESTORE]");
        logger.debug(application.getId());
	}

	/**
	 * @see com.thinkparity.browser.platform.application.ApplicationListener#notifyStart(com.thinkparity.browser.platform.application.Application)
	 * 
	 */
	public void notifyStart(final Application application) {
        logger.info("[LBROWSER] [PLATFORM] [NOTIFY APPLICATION START]");
        logger.debug(application.getId());
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
	    ApplicationFactory.create(this, ApplicationId.SYS_APP).start(this);
		login();
		if(isLoggedIn()) {
            ApplicationFactory.create(this, ApplicationId.BROWSER2).start(this);
		}
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
     * @see com.thinkparity.browser.platform.Platform#end()
     * 
     */
    public void end() {
        for(final ApplicationId id : ApplicationId.values()) {
            applicationRegistry.get(id).end(this);
        }
        System.exit(0);
    }
}
