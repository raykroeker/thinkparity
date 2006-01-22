/*
 * Jan 21, 2006
 */
package com.thinkparity.browser.util.l10n;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Localization helper used by abstract JFrames. To use this utility; you will
 * need to inherit from AbstractJFrame and use the wrapped getXXX apis; or
 * create an instance of this class within your JFrame.
 * 
 * The messages are located in JFrame_Messages_xx.properties
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JFrameLocalization {

	/**
	 * Resource bundle.
	 * 
	 */
	private static final ResourceBundle RESOURCE_BUNDLE;

	static {
		RESOURCE_BUNDLE =
			ResourceBundleManager.getBundle(ResourceBundleType.JFRAME);
	}

	/**
	 * Resource bundle helper.
	 * 
	 */
	protected final ResourceBundleHelper bundleHelper;

	/**
	 * Create a JFrameLocalization.
	 * 
	 */
	public JFrameLocalization(final String l18nContext) {
		super();
		this.bundleHelper =
			new ResourceBundleHelper(RESOURCE_BUNDLE, l18nContext);
	}

	/**
	 * Obtain a localized string.
	 * 
	 * @param localKey
	 *            The local key.
	 * @return The localized string.
	 * @see ResourceBundle#getString(java.lang.String)
	 */
	public String getString(final String localKey) {
		return bundleHelper.getString(localKey);
	}

	/**
	 * Obtain a localized string.
	 * 
	 * @param localKey
	 *            The local key.
	 * @param arguments
	 *            The arguments.
	 * @return The localized string.
	 * @see ResourceBundle#getString(java.lang.String)
	 * @see MessageFormat#format(java.lang.String, java.lang.Object[])
	 */
	public String getString(final String localKey, final Object[] arguments) {
		return bundleHelper.getString(localKey, arguments);
	}
}
