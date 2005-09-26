/*
 * Jul 2, 2005
 */
package com.thinkparity.codebase;

/**
 * ClassUtil
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ClassUtil {

	/**
	 * Represents an empty path.
	 */
	private static final StringBuffer emptyPath =
		new StringBuffer(0);

	/**
	 * Build a '<tt>/</tt>'-separated path for the given Class' package.  This
	 * path can then be used to load a resource.  If clasz is null, an empty
	 * path will be returned.
	 * @param clasz <code>java.lang.Class</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public static synchronized StringBuffer getPath(final Class clasz) {
		if(null == clasz)
			return emptyPath;
		return StringUtil.searchAndReplace(clasz.getPackage().getName(), ".",
				"/");
	}

	/**
	 * Create a ClassUtil [Singleton]
	 */
	private ClassUtil() { super(); }
}
