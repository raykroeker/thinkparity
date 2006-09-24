/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

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
