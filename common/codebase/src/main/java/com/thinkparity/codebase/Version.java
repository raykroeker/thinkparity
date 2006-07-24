/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase;

import com.thinkparity.codebase.config.Config;

/**
 * <b>Title:</b>thinkParity Version<br>
 * <b>Description:</b>An abstraction of version information for a thinkParity
 * application.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.2.4
 */
public abstract class Version {

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
