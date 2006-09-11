/*
 * Oct 7, 2005
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ResourceBundleManager
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ResourceBundleManager {

	/** A singleton instance. */
	private static final ResourceBundleManager SINGLETON;

	static { SINGLETON = new ResourceBundleManager(); }

	/**
	 * Obtain a resource bundle for a given resource bundle type.
	 * 
	 * @param bundleType
	 *            Type of resource bundle to obtain.
	 * @return The resource bundle.
	 */
	public static ResourceBundle getBundle(final ResourceBundleType bundleType) {
		return SINGLETON.doGetBundle(bundleType);
	}

	/**
	 * Create a ResourceBundleManager [Singleton]
	 * 
	 */
	private ResourceBundleManager() { super(); }

	/**
	 * Obtain a resource bundle for a given resource bundle type.
	 * 
	 * @param bundleType
	 *            The type of bundle to obtain.
	 * @return The resource bundle.
	 */
	private ResourceBundle doGetBundle(final ResourceBundleType bundleType) {
		return ResourceBundle.getBundle(getBaseName(bundleType), getLocale());
	}

	/**
	 * Build the base NAME for the given resource bundle type.
	 * 
	 * @param bundleType
	 *            The type of resource bundle to NAME.
	 * @return The base NAME used to obtain the resource bundle.
	 */
	private String getBaseName(final ResourceBundleType bundleType) {
		return new StringBuffer("localization")
			.append(".")
			.append(bundleType.getBaseName())
			.append("_Messages").toString();
	}

	/**
	 * Obtain the locale for the browser.
	 * 
	 * @return The locale for the browser.
	 */
	private Locale getLocale() { return LocaleManager.getLocale(); }
}
