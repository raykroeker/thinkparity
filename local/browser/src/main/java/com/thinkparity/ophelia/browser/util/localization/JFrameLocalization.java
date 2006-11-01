/*
 * Jan 21, 2006
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.thinkparity.codebase.log4j.Log4JWrapper;

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

	/** A <code>ResourceBundle</code>. */
	private static final ResourceBundle RESOURCE_BUNDLE;

	static {
		RESOURCE_BUNDLE =
			ResourceBundleManager.getBundle(ResourceBundleType.JFRAME);
	}

	/** A <code>ResourceBundleHelper</code>. */
	protected final ResourceBundleHelper bundleHelper;

	/** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    /**
     * Create JFrameLocalization.
     * 
     * @param l18nContext
     *            A localization context <code>String</code>.
     */
	public JFrameLocalization(final String l18nContext) {
		super();
        this.logger = new Log4JWrapper();
		this.bundleHelper =
			new ResourceBundleHelper(RESOURCE_BUNDLE, l18nContext);
	}

	/**
     * Obtain a localized integer.
     * 
     * @param localKey
     *            A local key <code>String</code>.
     * @return An <code>Integer</code>.
     */
    public Integer getInteger(final String localKey) {
        try {
            return Integer.valueOf(getString(localKey));
        } catch (final NumberFormatException nfx) {
            logger.logWarning("Cannot format {0} for key {1}.",
                    getString(localKey), localKey);
            return 0;
        }
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
