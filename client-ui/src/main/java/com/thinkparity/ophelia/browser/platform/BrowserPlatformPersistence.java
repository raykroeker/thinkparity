/*
 * Mar 16, 2006
 */
package com.thinkparity.ophelia.browser.platform;

import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.ophelia.browser.platform.util.persistence.Persistence;
import com.thinkparity.ophelia.browser.platform.util.persistence.PersistenceFactory;

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
	 * Create a BrowserPlatformPersistence.
	 */
	BrowserPlatformPersistence(final Platform platform) {
		super();
		this.persistence = PersistenceFactory.getPersistence(BrowserPlatform.class);
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
     * Obtain the platform locale.
     * 
     * @return The <code>Locale</code>.
     */
    public Locale getLocale() {
        return persistence.get("locale", Locale.getDefault());
    }

    /**
     * Obtain the platform <code>TimeZone</code>.
     * 
     * @return The <code>TimeZone</code>.
     */
    public TimeZone getTimeZone() {
        return persistence.get("timeZone", TimeZone.getDefault());
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

    /**
     * Set the platform locale.
     * 
     * @param locale
     *            The <code>Locale</code>.
     */
    public void setLocale(final Locale locale) {
        persistence.set("locale", locale);
    }

    /**
     * Set the platform <code>TimeZone</code>.
     * 
     * @param timeZone
     *            The <code>TimeZone</code>.
     */
    public void setTimeZone(final TimeZone timeZone) {
        persistence.set("timeZone", timeZone);
    }
}
