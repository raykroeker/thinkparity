/*
 * Sep 13, 2003
 */
package com.thinkparity.model;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * The lModel version info.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Version {

	/** Version.properties */
	private static final Config config;

	static { config = ConfigFactory.newInstance(Version.class); }

	/**
     * Obtain the build id.
     * 
     * @return The build id.
     */
	public static String getBuildId() { return config.getProperty("buildId"); }

	/**
     * Obtain the library name.
     * 
     * @return The library name.
     */
	public static String getName() { return config.getProperty("name"); }

	/**
	 * Obtain the library version.
	 * 
	 * @return The library version.
	 */
	public static String getVersion() {return config.getProperty("version"); }
}
