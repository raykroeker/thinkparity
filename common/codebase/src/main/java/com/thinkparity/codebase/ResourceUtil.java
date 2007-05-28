/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Locale;

/**
 * <b>Title:</b>  ResourceUtil
 * <br><b>Description:</b>  Provides helper methods for obtaining references
 * to resources.
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class ResourceUtil {

	/** The system's file encoding. */
    private static final String SYSTEM_FILE_ENCODING;

	static {
        SYSTEM_FILE_ENCODING = System.getProperty("file.encoding");
    }

	/**
     * Resolve a <code>File</code> for a named resource. The resource could be
     * either a file or a directory.
     * 
     * @param name
     *            The resource name <code>String</code>.
     * @return A <code>java.io.File</code>.
     */
	public static File getFile(final String name) {
		if (null == name)
			return null;
        try {
            final URL url = getURL(name);
            if (null == url)
                return null;
            final String pathname = URLDecoder.decode(
                    url.getFile(), SYSTEM_FILE_ENCODING);
            return new File(pathname);
        } catch (final UnsupportedEncodingException uex) {
            throw new RuntimeException(uex);
        }
	}

	/**
     * Obtain an InputStream for a resource
     * 
     * @param resourcePath
     *            <code>java.lang.String</code>
     * @return <code>java.io.InputStream</code>
     */
	public static InputStream getInputStream(String resourcePath) {
		if (null == resourcePath)
			return null;
		return ResourceUtil.class.getClassLoader().getResourceAsStream(
				resourcePath);
	}

    /**
     * Obtain an open an inputstream for a localized resource.
     * 
     * @param baseName
     *            A resource base name <code>String</code>.
     * @param locale
     *            A <code>Locale</code>.
     * @return An <code>InputStream</code>.
     */
    public static InputStream getLocalizedInputStream(final String baseName,
            final Locale locale) {
        URL resourceURL = getURL(new StringBuilder(baseName.toString())
                .append("_").append(locale.getLanguage())
                .append("_").append(locale.getCountry())
                .append("_").append(locale.getVariant()).toString());
        if (null == resourceURL) {
            resourceURL = getURL(new StringBuilder(baseName.toString())
                .append("_").append(locale.getLanguage())
                .append("_").append(locale.getCountry()).toString());
        }
        if (null == resourceURL) {
            resourceURL = getURL(new StringBuilder(baseName.toString())
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
	 * Obtain the url for a resource
	 * @param resourcePath <code>java.lang.String</code>
	 * @return <code>java.net.URL</code> or null if resource path is null
	 */
	public static URL getURL(String resourcePath) {
		if (null == resourcePath)
			return null;
		return ResourceUtil.class.getClassLoader().getResource(resourcePath);
	}

    /**
	 * Create a new ResourceUtil [Singleton]
	 */
	private ResourceUtil() { super(); }
}
