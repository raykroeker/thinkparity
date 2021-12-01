/*
 * Oct 7, 2005
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.util.Locale;

/**
 * LocaleManager
 * @author raymond@raykroeker.com
 * @version 1.0
 *
 */
public class LocaleManager {

	/**
	 * Singleton instance of the locale manager.
	 */
	private static final LocaleManager singleton =
		new LocaleManager();

	/**
	 * Synchronization lock for the singleton instance.
	 */
	private static final Object singletonLock = new Object();

	/**
	 * Obtain the locale for the browser.
	 * 
	 * @return The locale for the browser.
	 */
	public static Locale getLocale() {
		synchronized(singletonLock) { return singleton.getLocaleImpl(); }
	}

	/**
	 * Create a new LocaleManager [Singleton]
	 */
	private LocaleManager() { super(); }

	/**
	 * Obtain the locale for the browser.
	 * 
	 * @return The locale for the browser.
	 */
	private Locale getLocaleImpl() { return Locale.getDefault(); }
}
