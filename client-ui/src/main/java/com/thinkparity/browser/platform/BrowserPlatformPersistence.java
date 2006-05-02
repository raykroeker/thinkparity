/*
 * Mar 16, 2006
 */
package com.thinkparity.browser.platform;

import com.thinkparity.browser.platform.util.persistence.Persistence;
import com.thinkparity.browser.platform.util.persistence.PersistenceFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserPlatformPersistence {

	/**
	 * The platform's persistence.
	 * 
	 */
	private final Persistence persistence;

	/**
	 * The browser platform.
	 * 
	 */
	private final Platform platform;

	/**
	 * Create a BrowserPlatformPersistence.
	 */
	BrowserPlatformPersistence(final Platform platform) {
		super();
		this.persistence = PersistenceFactory.getPersistence(BrowserPlatform.class);
		this.platform = platform;
	}

	/**
	 * Determine whether or not auto-login is turned on.
	 * 
	 * @return True if it is set; false otherwise.
	 */
	public Boolean doAutoLogin() {
		return persistence.get("autoLogin", Boolean.FALSE);
	}

	/**
	 * Set auto-login to true.
	 * 
	 * @param password
	 *            The password to auto-login with.
	 */
	public void setAutoLogin(final Boolean autoLogin) {
		persistence.set("autoLogin", autoLogin);
	}
}
