/*
 * Created On: Sep 13, 2003
 * $Id$
 */
package com.thinkparity.desdemona.model;

import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Version<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public final class Version extends com.thinkparity.codebase.Version {

    /** The singleton instance. */
    private static final Version SINGLETON;

	static {
        SINGLETON = new Version(ConfigFactory.newInstance("version.properties"));
	}

    /**
     * Obtain the build id.
     * 
     * @return A build id <code>String</code>.
     */
    public static String getBuildId() {
        return SINGLETON.doGetBuildId();
    }

	/**
	 * Obtain the product name.
	 * 
	 * @return The product name <code>String</code>.
	 */
    public static String getProductName() {
        return SINGLETON.doGetProductName();
    }

    /**
     * Obtain the release name.
     *
     * @return The release name <code>String</code>.
     */
    public static String getReleaseName() {
        return SINGLETON.doGetReleaseName();
    }

    /**
     * Create Version.
     *
     * @param config
     *      The version <code>Config</code>.
     */
    private Version(final Config config) {
        super(config);
    }
}
