/*
 * Created On: Apr 9, 2004
 * $Id$
 */
package com.thinkparity.codebase.config;

import com.thinkparity.codebase.ResourceUtil;

/**
 * <b>Title:</b>thinkParity Common ConfigFactory </br>
 * <b>Description:</b> Provides a singleton implementation for obtaining the
 * configuration for an application.
 * 
 * @author raymond@raykroeker.com
 * @version 0.0.0
 */
public final class ConfigFactory {

    /**
     * Instantiate a configuration at a given resource path.
     * 
     * @param path
     *            A <code>String</code>.
     * @return An instance of <code>Config</code>.
     */
	public static Config newInstance(final String path) {
        return new Config(ResourceUtil.getURL(path));
    }

    /**
	 * Create ConfigFactory.
	 * 
	 */
	private ConfigFactory() {
        super();
    }
}
