/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.l10n;

import java.util.ResourceBundle;

import com.thinkparity.model.parity.api.ParityObjectType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JPanelLocalisation {

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
	private final ResourceBundleHelper bundleHelper;

	/**
	 * Create a JPanelLocalisation.
	 * 
	 * @param context
	 *            The named context for the JPanel.
	 */
	public JPanelLocalisation(final String context) {
		super();
		this.bundleHelper = new ResourceBundleHelper(RESOURCE_BUNDLE, context);
	}

	/**
	 * Obtain the localised string for an artifact type.
	 * 
	 * @param artifactType
	 *            The parity artifact type.
	 * @return The localised text.
	 */
	public String getString(final ParityObjectType artifactType) {
		return getString(artifactType.toString());
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
