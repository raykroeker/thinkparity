/*
 * Oct 7, 2005
 */
package com.thinkparity.browser.util.l10n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ResourceBundleManager
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ResourceBundleManager {

	/**
	 * Singleton instance of the resource manager.
	 */
	private static final ResourceBundleManager singleton =
		new ResourceBundleManager();

	/**
	 * Synchronization lock for access to the singleton.
	 */
	private static final Object singletonLock = new Object();

	/**
	 * Obtain a resource bundle for a given resource bundle type.
	 * 
	 * @param resourceBundleType
	 *            Type of resource bundle to obtain.
	 * @return The resource bundle.
	 */
	public static ResourceBundle getBundle(
			final ResourceBundleType resourceBundleType) {
		synchronized(singletonLock) {
			return singleton.doGetBundle(resourceBundleType);
		}
	}

	/**
	 * Prefix of each of the bundle names.
	 */
	private final String baseNamePrefix;

	/**
	 * Create a ResourceBundleManager [Singleton]
	 */
	private ResourceBundleManager() {
		super();
		this.baseNamePrefix = getClass().getPackage().getName();
	}

	/**
	 * Build the base name for the given resource bundle type.
	 * 
	 * @param resourceBundleType
	 *            The type of resource bundle to name.
	 * @return The base name used to obtain the resource bundle.
	 */
	private String buildBaseName(final ResourceBundleType resourceBundleType) {
		switch (resourceBundleType) {
		case ACTION:
			return baseNamePrefix + ".Action_Messages";
		case DIALOG:
			return baseNamePrefix + ".Dialog_Messages";
		case JPANEL:
			return baseNamePrefix + ".JPanel_Messages";
		case TRAY:
			return baseNamePrefix + ".Tray_Messages";
		case VIEW:
			return baseNamePrefix + ".View_Messages";
		default:
			return baseNamePrefix + ".Messages";
		}
	}

	/**
	 * Obtain a resource bundle for a given resource bundle type.
	 * 
	 * @param type
	 *            The type of bundle to obtain.
	 * @return The resource bundle.
	 */
	private ResourceBundle doGetBundle(final ResourceBundleType type) {
		final ResourceBundle resourceBundle =
			ResourceBundle.getBundle(buildBaseName(type), getLocale());
		return resourceBundle;
	}

	/**
	 * Obtain the locale for the browser.
	 * @return The locale for the browser.
	 */
	private Locale getLocale() { return LocaleManager.getLocale(); }

}
