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
     * @return The build id <code>String</code>.
     */
	protected String doGetBuildId() {
        return config.getProperty("thinkparity.build-id");
	}

	/**
     * Obtain the release name.
     * 
     * @return The release name <code>String</code>.
     */
	protected String doGetReleaseName() {
        return config.getProperty("thinkparity.release-name");
	}

    /**
     * Obtain the product name.
     * 
     * @return The product name <code>String</code>.
     */
	protected String doGetProductName() {
        return config.getProperty("thinkparity.product-name");
	}
}
