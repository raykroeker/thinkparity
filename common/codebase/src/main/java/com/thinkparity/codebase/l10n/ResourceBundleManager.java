/*
 * Oct 7, 2005
 */
package com.thinkparity.codebase.l10n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ResourceBundleManager
 * 
 * @author raymond@raykroeker.com
 * @version 1.0
 */
public class ResourceBundleManager {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final ResourceBundleManager singleton;

	static { singleton = new ResourceBundleManager(); }

	/**
     * Obtain a resource bundle for a given localization resource.
     * 
     * @param l18nResource
     *            A localization resource.
     * @return A resource bundle.
     */
	public static ResourceBundle getBundle(final L18nResource l18nResource) {
		return singleton.doGetBundle(l18nResource);
	}

	/**
	 * Create a ResourceBundleManager [Singleton]
	 * 
	 */
	private ResourceBundleManager() { super(); }

	/**
	 * Obtain a resource bundle for a given localization resource.
	 * 
	 * @param l18nResource
	 *            A localization resource.
	 * @return The resource bundle.
	 */
	private ResourceBundle doGetBundle(final L18nResource l18nResource) {
		return ResourceBundle.getBundle(
				l18nResource.getResourceBundleBaseName(), getLocale());
	}

	/**
	 * Build the base NAME for the given resource bundle type.
	 * 
	 * @param bundleType
	 *            The type of resource bundle to NAME.
	 * @return The base NAME used to obtain the resource bundle.
	 */
//	private String getBaseName(final L18nType l18nType,
//			final Package javaPackage) {
//		return new StringBuffer(javaPackage.getName())
//			.append(".")
//			.append(l18nType.getBaseName())
//			.append("_Messages").toString();
//	}

	/**
	 * Obtain the locale for the browser.
	 * 
	 * @return The locale for the browser.
	 */
	private Locale getLocale() { return LocaleManager.getLocale(); }
}
