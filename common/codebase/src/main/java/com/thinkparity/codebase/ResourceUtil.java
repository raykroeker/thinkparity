/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * <b>Title:</b>  ResourceUtil
 * <br><b>Description:</b>  Provides helper methods for obtaining references
 * to resources.
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class ResourceUtil {

	/**
	 * Create a new ResourceUtil [Singleton]
	 */
	private ResourceUtil() { super(); }

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
	 * Obtain an InputStream for a resource
	 * @param resourcePath <code>java.lang.String</code>
	 * @return <code>java.io.InputStream</code>
	 */
	public static InputStream getInputStream(String resourcePath) {
		if (null == resourcePath)
			return null;
		return ResourceUtil.class.getClassLoader().getResourceAsStream(
				resourcePath);
	}

	/**
	 * Obtain a File for a resource
	 * @param resourcePath <code>java.lang.String</code>
	 * @return <code>java.io.File</code>
	 */
	public static File getFile(String resourcePath) {
		if (null == resourcePath)
			return null;
		return new File(getURL(resourcePath).getFile());
	}

}
