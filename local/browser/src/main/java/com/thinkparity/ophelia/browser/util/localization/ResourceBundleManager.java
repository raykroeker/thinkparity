/*
 * Oct 7, 2005
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import com.thinkparity.codebase.ResourceUtil;

/**
 * ResourceBundleManager
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ResourceBundleManager {

	/** A singleton instance. */
	private static final ResourceBundleManager SINGLETON;

	static { SINGLETON = new ResourceBundleManager(); }

	/**
	 * Obtain a resource bundle for a given resource bundle type.
	 * 
	 * @param bundleType
	 *            Type of resource bundle to obtain.
	 * @return The resource bundle.
	 */
	public static ResourceBundle getBundle(final ResourceBundleType bundleType) {
		return SINGLETON.doGetBundle(bundleType);
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
     * Obtain a resource bundle for a given resource bundle type.
     * 
     * @param bundleType
     *            The type of bundle to obtain.
     * @return The resource bundle.
     */
    private ResourceBundle doGetBundle(final ResourceBundleType bundleType) {
        return ResourceBundle.getBundle(getBaseName(bundleType), getLocale());
    }

    /**
     * Obtain a resource bundle for a given resource bundle type.
     * 
     * @param bundleType
     *            The type of bundle to obtain.
     * @return The resource bundle.
     */
    private InputStream doOpenResource(final String name) {
        final StringBuilder baseName = new StringBuilder("localization")
            .append("/").append(name);
        final Locale locale = getLocale();
        URL resourceURL = ResourceUtil.getURL(new StringBuilder(baseName.toString())
                .append("_").append(locale.getLanguage())
                .append("_").append(locale.getCountry())
                .append("_").append(locale.getVariant()).toString());
        if (null == resourceURL) {
            resourceURL = ResourceUtil.getURL(new StringBuilder(baseName.toString())
                .append("_").append(locale.getLanguage())
                .append("_").append(locale.getCountry()).toString());
        }
        if (null == resourceURL) {
            resourceURL = ResourceUtil.getURL(new StringBuilder(baseName.toString())
                .append("_").append(locale.getLanguage()).toString());
        }
        if (null == resourceURL) {
            resourceURL = ResourceUtil.getURL(baseName.toString());
        }
        try {
            return resourceURL.openStream();
        } catch (final IOException iox) {
            return null;
        }
    }

	/**
	 * Build the base NAME for the given resource bundle type.
	 * 
	 * @param bundleType
	 *            The type of resource bundle to NAME.
	 * @return The base NAME used to obtain the resource bundle.
	 */
	private String getBaseName(final ResourceBundleType bundleType) {
		return new StringBuffer("localization")
			.append(".")
			.append(bundleType.getBaseName())
			.append("_Messages").toString();
	}

	/**
	 * Obtain the locale for the browser.
	 * 
	 * @return The locale for the browser.
	 */
	private Locale getLocale() { return LocaleManager.getLocale(); }
}
