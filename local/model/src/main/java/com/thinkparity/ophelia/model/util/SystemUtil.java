/*
 * Aug 8, 2005
 */
package com.thinkparity.ophelia.model.util;

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
	public static String getenv(String name) { return getenvImpl(name); }

	/**
	 * Check to see if there exists an system-property override for the named
	 * environment variable. This will do a lookup in the envNameToPropertyMap
	 * for name, then check the system property for a match.
	 * 
	 * @param name
	 *            The environment variable name.
	 * @return The system property override (if one exists) or the environment
	 *         variable value.
	 */
    private static String getenvImpl(final String name) {
    	final String override = System.getProperty(internalGetPropertyKey(name));
    	if(null == override) { return System.getenv(name); }
    	else { return override; }
    }

    /**
	 * Obtain the system property name mapped to the env variable name.
	 * 
	 * @param name
	 *            The environment variable name.
	 * @return The name of the system property that can override the environment
	 *         variable name.
	 */
    private static String internalGetPropertyKey(final String name) {
    	return envNameToPropertyMap.get(name);
    }
	/**
	 * Create a SystemUtil [Singleton]
	 */
	private SystemUtil() { super(); }
}
