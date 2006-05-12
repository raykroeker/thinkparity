/*
 * Created On: Apr 9, 2004
 * $Id$
 */
package com.thinkparity.codebase.config;

import java.io.File;

import com.thinkparity.codebase.ResourceUtil;

/**
 * <b>Title:</b>  ConfigFactory
 * <br><b>Description:</b>  Provides a singleton implementation for obtaining the
 * configuration for an application.
 * @author raykroeker@gmail.com
 * @version 0.0.0
 */
public abstract class ConfigFactory {

	public static synchronized Config newInstance() {return new Config();}
	
	public static synchronized Config newInstance(Class clasz) {
		return new Config(clasz, System.getProperties());
	}
	
	public static synchronized Config newInstance(Class clasz, Config defaultConfig) {
		return new Config(clasz, defaultConfig);
	}
	
	public static synchronized Config newInstance(Config defaultConfig) {
		return new Config(defaultConfig);
	}

	public static synchronized Config newInstance(File file) {
		return new Config(file);
	}
	
	public static synchronized Config newInstance(File file, Config defaultConfig) {
		return new Config(file, defaultConfig);
	}

	public static Config newInstance(final String resourcePath) {
        return new Config(ResourceUtil.getURL(resourcePath));
    }

    public static Config newInstance(final String resourcePath,
            final Config defaultConfig) {
        return new Config(ResourceUtil.getURL(resourcePath), defaultConfig);
    }

    /**
	 * Create a new ConfigFactory [Singleton]
	 */
	private ConfigFactory() {super();}
}
