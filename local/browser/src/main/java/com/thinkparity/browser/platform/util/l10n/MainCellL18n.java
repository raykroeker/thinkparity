/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform.util.l10n;

import java.util.ResourceBundle;

import com.thinkparity.model.parity.model.artifact.ArtifactType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainCellL18n {

	/**
	 * Resource bundle.
	 * 
	 */
	private static final ResourceBundle RESOURCE_BUNDLE;

	static {
		RESOURCE_BUNDLE =
			ResourceBundleManager.getBundle(ResourceBundleType.LIST_ITEM);
	}

	/**
	 * Resource bundler helper.
	 * 
	 */
	protected final ResourceBundleHelper bundleHelper;

	/**
	 * Create an ActionLocalization.
	 * 
	 * @param context
	 *            The named context for the Action.
	 */
	public MainCellL18n(final String l18nContext) {
		super();
		this.bundleHelper = new ResourceBundleHelper(RESOURCE_BUNDLE, l18nContext);
	}

	/**
	 * Obtain the localised string for an artifact type.
	 * 
	 * @param artifactType
	 *            The parity artifact type.
	 * @return The localised text.
	 */
	public String getString(final ArtifactType artifactType) {
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
