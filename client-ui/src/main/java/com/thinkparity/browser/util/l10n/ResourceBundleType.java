/*
 * Oct 7, 2005
 */
package com.thinkparity.browser.util.l10n;

/**
 * A resource bundle type is a way of logically separating the localization into
 * groups. Each group is assigned 1 resource bundle based upon the type's base
 * name.
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 * 
 * @see ResourceBundleManager#getBundle(ResourceBundleType)
 */
public enum ResourceBundleType {

	/**
	 * Resource bundle types.
	 * 
	 */
	ACTION("Action"), DIALOG("Dialog"), JFRAME("JFrame"), JPANEL("JPanel"),
	TRAY("Tray"), VIEW("View");

	/**
	 * The resource bundle base name.
	 * 
	 */
	private final String baseName;

	/**
	 * Create a ResourceBundleType.
	 * 
	 * @param baseName
	 *            The resource bundle base name.
	 */
	private ResourceBundleType(final String baseName) {
		this.baseName = baseName;
	}

	/**
	 * Obtain the base name.
	 * 
	 * @return The base name.
	 */
	public String getBaseName() { return baseName; }
}
