/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.platform.application;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.system.SystemApplication;
import com.thinkparity.browser.platform.Platform;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ApplicationFactory {

	/**
	 * The singleton application factory.
	 * 
	 */
	private static final ApplicationFactory SINGLETON;

	static { SINGLETON = new ApplicationFactory(); }

	/**
	 * Create an application.
	 * 
	 * @param id
	 *            The application id.
	 * @return The applcation.
	 */
	public static Application create(final Platform platform,
			final ApplicationId id) {
		return SINGLETON.doCreate(platform, id);
	}

	/**
	 * The application registry.
	 * 
	 */
	private final ApplicationRegistry registry;

	/**
	 * Create an ApplicationFactory [Singleton, Factory]
	 * 
	 */
	private ApplicationFactory() {
		super();
		this.registry = new ApplicationRegistry();
	}

	/**
	 * Create the browser application.
	 * 
	 * @return The browser application.
	 */
	private Application createBrowser2(final Platform platform) {
		final Application browser2 = new Browser(platform);
		browser2.addListener(platform);
		return browser2;
	}

	/**
	 * Create the session application.
	 *
	 * @return The session application.
	 */
	private Application createSysApp(final Platform platform) {
		final Application sysApp = new SystemApplication(platform);
		sysApp.addListener(platform);
		return sysApp;
	}

	/**
	 * Create the application specified by the id and register it.
	 * 
	 * @param id
	 *            The application id.
	 * @return The application.
	 */
	private Application doCreate(final Platform platform, final ApplicationId id) {
		final Application application;
		switch(id) {
		case BROWSER2:
			application = createBrowser2(platform);
			break;
		case SYS_APP:
			application = createSysApp(platform);
			break;
		default: throw Assert.createUnreachable("Unknown application:  " + id);
		}
		register(application);
		return application;
	}

	/**
	 * Register the application.
	 * 
	 * @param application
	 *            The application to register.
	 */
	private void register(final Application application) {
		Assert.assertNotTrue(
				"[LBROWSER] [PLATFORM] [APP FACTORY] [APPLICATION ALREADY REGISTERED]",
				registry.contains(application.getId()));
		registry.put(application);
	}
}
