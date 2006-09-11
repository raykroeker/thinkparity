/*
 * Sep 13, 2003
 */
package com.thinkparity.ophelia.browser;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;
import com.thinkparity.codebase.Mode;

/**
 * Version
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public class Version extends com.thinkparity.codebase.Version {

    /** The singleton instance. */
    private static final Version SINGLETON;

	static {
        SINGLETON = new Version(ConfigFactory.newInstance("version.properties"));
    }

    /**
     * Create a Version.
     *
     * @param config
     *      The version properties.
     */
    private Version(final Config config) { super(config); }

	/**
	 * Obtain the build id.
	 * 
	 * @return A build id string.
	 */
    public static String getBuildId() { return SINGLETON.doGetBuildId(); }

    /**
     * Obtain the operating mode.
     *
     * @return An operating mode.
     */
    public static Mode getMode() { return SINGLETON.doGetMode(); }

	/**
	 * Obtain the name.
	 * 
	 * @return A name string.
	 */
	public static  String getName() { return SINGLETON.doGetName(); }

    /**
     * Obtain the release id.
     *
     * @return A release id.
     */
    public static String getReleaseId() { return SINGLETON.doGetReleaseId(); }

	/**
	 * Obtain the version.
	 * 
	 * @return A version string.
	 */
    public static  String getVersion() { return SINGLETON.doGetVersion(); }
}
