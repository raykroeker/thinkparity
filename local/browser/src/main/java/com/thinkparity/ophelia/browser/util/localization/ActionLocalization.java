/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.util.ResourceBundle;

import com.thinkparity.codebase.model.artifact.ArtifactType;

import com.thinkparity.ophelia.browser.platform.plugin.PluginExtension;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ActionLocalization {

	/**
	 * Resource bundle.
	 * 
	 */
	private static final ResourceBundle RESOURCE_BUNDLE;

	static {
		RESOURCE_BUNDLE =
			ResourceBundleManager.getBundle(ResourceBundleType.ACTION);
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
    public ActionLocalization(final String l18nContext) {
        super();
        this.bundleHelper = new ResourceBundleHelper(RESOURCE_BUNDLE, l18nContext);
    }

    /**
     * Create an ActionLocalization.
     * 
     * @param extension
     *            A plugin extension.
     */
    public ActionLocalization(final PluginExtension extension) {
        super();
        this.bundleHelper = new ResourceBundleHelper(
                extension.getLocalization(), extension.getLocalizationContext());
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
