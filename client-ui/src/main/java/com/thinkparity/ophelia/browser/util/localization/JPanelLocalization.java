/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.io.InputStream;
import java.util.ResourceBundle;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JPanelLocalization {

	/**
	 * Resource bundle.
	 * 
	 */
	private static final ResourceBundle RESOURCE_BUNDLE;

	static {
		RESOURCE_BUNDLE =
			ResourceBundleManager.getBundle(ResourceBundleType.JPANEL);
	}

	/**
	 * Resource bundler helper.
	 * 
	 */
	protected final ResourceBundleHelper bundleHelper;

	/**
	 * Create a JPanelLocalization.
	 * 
	 * @param context
	 *            The named context for the JPanel.
	 */
	public JPanelLocalization(final String l18nContext) {
		super();
		this.bundleHelper = new ResourceBundleHelper(RESOURCE_BUNDLE, l18nContext);
	}

    /**
     * Obtain the localised string for an enumerated type.
     * 
     * @param type
     *            An enumerated type.
     * @return The localised text.
     */
    public String getString(final Enum<?> type) {
        return getString(type.toString());
    }

    /**
     * Obtain the localised string for an enumerated type.
     * 
     * @param type
     *            An enumerated type.
     * @return The localised text.
     */
    public String getString(final Enum<?> type, final Object... arguments) {
        return getString(type.toString(), arguments);
    }

	/**
	 * Obtain a localised string.
	 * 
	 * @param localKey
	 *            The key within the given context.
	 * @return The localised text.
	 */
	public String getString(final String localKey) {
		return bundleHelper.getString(localKey);
	}

    /**
     * Open a localized resource.
     * 
     * @param name
     *            The resource name <code>String</code>.
     * @return An <code>InputStream</code>.
     */
    public final InputStream openResource(final String name) {
        return ResourceBundleManager.openResource(name);
    }

	/**
	 * Obtain the widget text for a named widget.
	 * 
	 * @param localKey
	 *            The key within the given context.
	 * @param arguments
	 *            The the arguments for the pattern.
	 * @return The localised text.
	 */
	public String getString(final String localKey, final Object[] arguments) {
		return bundleHelper.getString(localKey, arguments);
	}
}
