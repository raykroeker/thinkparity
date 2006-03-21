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
public class Browser2PlatformPersistence {

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
	 * Create a Browser2PlatformPersistence.
	 */
	Browser2PlatformPersistence(final Platform platform) {
		super();
		this.persistence = PersistenceFactory.getPersistence(Browser2Platform.class);
		this.platform = platform;
	}

	/**
	 * Determine whether or not auto-login is turned on.
	 * 
	 * @return True if it is set; false otherwise.
	 */
	public Boolean isSetAutoLogin() {
		return persistence.get("autoLogin", Boolean.FALSE);
	}

	/**
	 * Set auto-login to true.
	 * 
	 * @param password
	 *            The password to auto-login with.
	 */
	public void setAutoLogin(final String password) {
		persistence.set("autoLogin", Boolean.TRUE);
		platform.getPreferences().clearPassword();
		platform.getPreferences().setPassword(password);
	}
}
