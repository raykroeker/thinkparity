/*
 * Mar 4, 2006
 */
package com.thinkparity.ophelia.model.util.localization;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum L18nResource implements com.thinkparity.codebase.l10n.L18nResource {

	MODEL("Model");

	/**
	 * The resource name.
	 * 
	 */
	private final String resourceName;

	/**
	 * Create a L18nResource.
	 * 
	 * @param resourceName
	 *            The resource name.
	 */
	private L18nResource(final String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @see com.thinkparity.codebase.l10n.L18nContext#getResourceBundleBaseName()
	 */
	public String getResourceBundleBaseName() {
		return new StringBuffer("localization.")
			.append(resourceName)
			.append("_Messages")
			.toString();
	}
}
