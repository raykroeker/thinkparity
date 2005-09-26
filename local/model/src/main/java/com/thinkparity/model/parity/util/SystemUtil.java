/*
 * Aug 8, 2005
 */
package com.thinkparity.model.parity.util;

import java.util.Hashtable;
import java.util.Map;

/**
 * SystemUtil
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemUtil {

	private static final Map<String, String> envNameToPropertyMap =
		new Hashtable<String, String>(1, 1.0F);
	static {
		envNameToPropertyMap.put("APPDATA", "parity.workspace");
		envNameToPropertyMap.put("HOME", "parity.workspace");
	}

	/**
	 * Provides an internal override if applicable.
	 * @see java.lang.System#getenv(java.lang.String)
	 */
	public static String getenv(String name) { return internalGetenv(name); }

    private static String internalGetenv(final String name) {
    	final String override = System.getProperty(internalGetPropertyKey(name));
    	if(null == override) { return System.getenv(name); }
    	else { return override; }
    }

    /**
     * Obtain the property name mapped to the env variable name.
     * @param name <code>String</code>
     * @return <code>String</code>
     */
    private static String internalGetPropertyKey(final String name) {
    	return envNameToPropertyMap.get(name);
    }
	/**
	 * Create a SystemUtil [Singleton]
	 */
	private SystemUtil() { super(); }
}
