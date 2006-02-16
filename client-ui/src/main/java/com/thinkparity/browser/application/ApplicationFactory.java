/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.application;

import com.thinkparity.browser.Browser2;
import com.thinkparity.browser.platform.Platform;
import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.application.ApplicationId;

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
	private static final ApplicationFactory singleton;

	static { singleton = new ApplicationFactory(Browser2.getPlatform()); }

	/**
	 * Create an application.
	 * 
	 * @param applicationId
	 *            The application to create.
	 * @return The applcation.
	 */
	public static Application create(final ApplicationId applicationId) {
		switch(applicationId) {
		case BROWSER:
			return singleton.createBrowser();
		case GADGET:
			return singleton.createGadget();
		case SESSION:
			return singleton.createSession();
		default:
			throw Assert.createUnreachable("");
		}
	}

	/**
	 * The browser platform.
	 * 
	 */
	private final Platform platform;

	/**
	 * Create an ApplicationFactory [Singleton, Factory]
	 * 
	 */
	private ApplicationFactory(final Platform platform) {
		super();
		this.platform = platform;
	}

	/**
	 * Create the browser application.
	 * 
	 * @return The browser application.
	 */
	private Application createBrowser() {
		return com.thinkparity.browser.application.browser.Browser.getInstance();
	}

	/**
	 * Create the gadget application.
	 * 
	 * @return The gadget application.
	 */
	private Application createGadget() {
		return com.thinkparity.browser.application.gadget.Gadget.createInstance(platform);
	}

	/**
	 * Create the session application.
	 *
	 * @return The session application.
	 */
	private Application createSession() {
		return com.thinkparity.browser.application.session.Session.getSession();
	}
}
