/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * <b>Title:</b>  Version
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class Version {

	/**
	 * Handle to the version config file
	 */
	private static final Config versionConfig =
		ConfigFactory.newInstance(Version.class);

	/**
	 * Obtain the version of the application
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getVersion() {
		return versionConfig.getProperty("version");
	}

	/**
	 * Obtain the name of the application
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getName() {
		return versionConfig.getProperty("name");
	}
}
