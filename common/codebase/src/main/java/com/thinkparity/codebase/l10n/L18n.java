/*
 * Mar 4, 2006
 */
package com.thinkparity.codebase.l10n;

import java.util.ResourceBundle;

/**
 * 
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class L18n {

	/**
     * Create a new resource bundle helper.
     * 
     * @param l18nResource
     *            A localization resource.
     * @param l18nContext
     *            A localization context.
     * @return A resource bundle helper.
     */
	private static ResourceBundleHelper createBundleHelper(
			final L18nResource l18nResource, final L18nContext l18nContext) {
		return new ResourceBundleHelper(getBundle(l18nResource), l18nContext);
	}

	/**
     * Obtain a resource bundle.
     * 
     * @param l18nContext
     *            A localization context.
     * @return A resource bundle.
     */
	private static ResourceBundle getBundle(final L18nResource l18nContext) {
		return ResourceBundleManager.getBundle(l18nContext);
	}

	/**
	 * The resource bundle helper.
	 * 
	 */
	protected final ResourceBundleHelper bundleHelper;

	/**
     * Create L18n.
     * 
     * @param l18nResource
     *            The localization resource.
     * @param l18nContext
     *            The localization context.
     * @see L18nResource
     * @see L18nContext
     */
	public L18n(final L18nResource l18nResource,
			final L18nContext l18nContext) {
		super();
		this.bundleHelper = createBundleHelper(l18nResource, l18nContext);
	}

	/**
	 * Obtain a localized string.
	 * 
	 * @param localKey
	 *            The key within the localization context.
	 * @return The localized string.
	 */
	public String getString(final String localKey) {
		return bundleHelper.getString(localKey);
	}

	/**
	 * Obtain a localized string.
	 * 
	 * @param localKey
	 *            The key within the localization context.
	 * @param arguments
	 *            Arguments for the localization formattting.
	 * @return The localized formatted string.
	 */
	public String getString(final String localKey, final Object[] arguments) {
		return bundleHelper.getString(localKey, arguments);
	}
}
