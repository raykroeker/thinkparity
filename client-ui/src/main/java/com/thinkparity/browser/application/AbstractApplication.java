/*
 * Feb 4, 2006
 */
package com.thinkparity.browser.application;

import com.thinkparity.browser.platform.application.Application;
import com.thinkparity.browser.platform.util.persistence.Persistence;
import com.thinkparity.browser.platform.util.persistence.PersistenceFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractApplication implements Application {

	/**
	 * Provides preferences persistence for the application.
	 * 
	 */
	private final Persistence persistence;

	/**
	 * Create an AbstractApplication.
	 * 
	 */
	protected AbstractApplication() {
		super();
		this.persistence = PersistenceFactory.getPersistence(getClass());
	}

	protected Boolean getPref(final String key, final Boolean defaultValue) {
		return persistence.get(key, defaultValue);
	}

	protected String getPref(final String key, final String defaultValue) {
		return persistence.get(key, defaultValue);
	}

	protected void setPref(final String key, final Boolean value) {
		persistence.set(key, value);
	}

	protected String setPref(final String key, final String value) {
		final String p = getPref(key, (String) null);
		persistence.set(key, value);
		return p;
	}
}
