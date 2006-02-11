/*
 * Sep 13, 2003
 */
package com.thinkparity.model;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * Version
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public class Version {

	/**
	 * Handle to the version config file
	 * @see Version#configLock
	 */
	private static final Config config;

	/**
	 * Synchronization lock for config.
	 * @see Version#config
	 */
	private static final Object configLock;

	static {
		config = ConfigFactory.newInstance(Version.class);
		configLock = new Object();
	}

	/**
	 * Obtain the build id of the parity model.
	 * 
	 * @return The build id of the parity model.
	 */
	public static String getBuildId() { return config.getProperty("buildId"); }

	/**
	 * Obtain the name of the parity model.
	 * 
	 * @return The name of the parity model.
	 */
	public static String getName() { return config.getProperty("name"); }

	/**
	 * Obtain the version of the parity model.
	 * 
	 * @return The version of the parity model.
	 */
	public static String getVersion() {return config.getProperty("version"); }
}
