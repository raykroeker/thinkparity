/*
 * Oct 7, 2005
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.ResourceUtil;

/**
 * ResourceBundleManager
 * @author raymond@raykroeker.com
 * @version 1.0
 */
public class ResourceBundleManager {

	/** A singleton instance. */
	private static final ResourceBundleManager SINGLETON;

	static { SINGLETON = new ResourceBundleManager(); }

	/**
     * Obtain a resource bundle for a given localization resource.
     * 
     * @param l18nResource
     *            A localization resource.
     * @return A resource bundle.
	 */
	public static ResourceBundle getBundle(final com.thinkparity.codebase.l10n.L18nResource l18nResource) {
		return SINGLETON.doGetBundle(l18nResource);
	}

    /**
     * Open a localized resource.
     * 
     * @param bundleType
     *            A <code>ResourceBundleType</code>.
     * @return An <code>InputStream</code>.
     * @throws IOException
     *             if an io exception occurs while opening the resource
     */
    public static InputStream openResource(final String name) {
        return SINGLETON.doOpenResource(name);
    }

	/**
	 * Create a ResourceBundleManager [Singleton]
	 * 
	 */
	private ResourceBundleManager() { super(); }

    /**
     * Obtain a resource bundle for a given localization resource.
     * 
     * @param l18nResource
     *            A localization resource.
     * @return The resource bundle.
     */
    private ResourceBundle doGetBundle(final com.thinkparity.codebase.l10n.L18nResource l18nResource) {
        return ResourceBundle.getBundle(
                l18nResource.getResourceBundleBaseName(), getLocale());
    }

    /**
     * Obtain a resource bundle for a given resource bundle type.
     * 
     * @param bundleType
     *            The type of bundle to obtain.
     * @return The resource bundle.
     */
    private InputStream doOpenResource(final String name) {
        return ResourceUtil.getLocalizedInputStream("localization/" + name, getLocale());
    }

	/**
	 * Obtain the locale for the browser.
	 * 
	 * @return The locale for the browser.
	 */
	private Locale getLocale() { return LocaleManager.getLocale(); }
}
