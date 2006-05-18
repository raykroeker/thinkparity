/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * The version class is used to obtain name\version info from the codebase
 * library.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.2.4
 */
public class Version {

	/** A singleton instance. */
	private static final Version SINGLETON;

	static { SINGLETON = new Version(ConfigFactory.newInstance(Version.class)); }

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

    /** The version properties file. */
    private final Config config;

    /**
     * Create a Version.
     *
     * @param config
     *      A version properties file.
     */
    protected Version(final Config config) {
        super();
        this.config = config;
    }

	/**
	 * Obtain the build id.
	 * 
	 * @return A build id string.
	 */
	protected String doGetBuildId() { return config.getProperty("com.thinkparity.parity.buildId"); }

    /**
     * Obtain an apache info log statement.
     * 
     * @return A log message.
     */
    protected String doGetInfo() {
        return new StringBuffer("BuildId:")
                .append(doGetBuildId())
                .append(",Mode:")
                .append(doGetMode().toString())
                .append(",Name:")
                .append(doGetName())
                .append(",Version:")
                .append(doGetVersion()).toString();
    }

	/**
     * Obtain the operating mode.
     *
     * @return An operating mode.
     */
    protected Mode doGetMode() {
        return Mode.valueOf(config.getProperty("com.thinkparity.parity.mode"));
    }

	/**
	 * Obtain the name.
	 * 
	 * @return A name string.
	 */
	protected String doGetName() { return config.getProperty("com.thinkparity.parity.name"); }

    /**
     * Obtain the release id.
     *
     * @return A release id.
     */
    protected String doGetReleaseId() { return config.getProperty("com.thinkparity.parity.releaseId"); }

    /**
	 * Obtain the version.
	 * 
	 * @return A version string.
	 */
	protected String doGetVersion() { return config.getProperty("com.thinkparity.parity.version"); }
}
