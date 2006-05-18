/*
 * May 9, 2006 11:57:28 AM
 * $Id$
 */
package com.thinkparity.migrator;

import com.thinkparity.codebase.Mode;
import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class Version extends com.thinkparity.codebase.Version {

    /** The singleton instance. */
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
    public static String getName() { return SINGLETON.doGetName(); }

    /**
     * Obtain the version.
     * 
     * @return A version string.
     */
    public static String getVersion() { return SINGLETON.doGetVersion(); }

    /**
     * Obtain an apache loggable info statement.
     * 
     * @return An info string.
     */
    public static String toInfo() { return SINGLETON.doGetInfo(); }

    /**
     * Obtain the release id.
     *
     * @return A release id.
     */
    public static String getReleaseId() { return SINGLETON.doGetReleaseId(); }

    /**
     * Create a Version.
     *
     * @param config
     *      The version properties.
     */
    private Version(final Config config) { super(config); }
}
