/*
 * Oct 7, 2005
 */
package com.thinkparity.browser.platform.util.l10n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ResourceBundleManager
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ResourceBundleManager {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final ResourceBundleManager INSTANCE;

	/**
	 * The package NAME is used to locate the localization resources.
	 * 
	 */
	private static final String PACKAGE_NAME;

	static {
		INSTANCE = new ResourceBundleManager();

		PACKAGE_NAME = ResourceBundleManager.class.getPackage().getName();
	}

	/**
	 * Obtain a resource bundle for a given resource bundle type.
	 * 
	 * @param bundleType
	 *            Type of resource bundle to obtain.
	 * @return The resource bundle.
	 */
	public static ResourceBundle getBundle(final ResourceBundleType bundleType) {
		return INSTANCE.doGetBundle(bundleType);
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
		return new StringBuffer(PACKAGE_NAME)
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
