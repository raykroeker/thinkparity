/*
 * Mar 20, 2006
 */
package com.thinkparity.ophelia.browser.util.localization;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum L18nResource implements com.thinkparity.codebase.l10n.L18nResource {

	APPLICATION("Application");

	/**
	 * The resource name.
	 * 
	 */
	private final String resourceName;

	/**
	 * Create a L18nResource.
	 */
	private L18nResource(final String resourceName) {
		this.resourceName = resourceName;
	}

	/**
	 * @see com.thinkparity.codebase.l10n.L18nResource#getResourceBundleBaseName()
	 */
	public String getResourceBundleBaseName() {
		return new StringBuffer("localization.")
			.append(resourceName)
			.append("_Messages")
			.toString();
	}
}
