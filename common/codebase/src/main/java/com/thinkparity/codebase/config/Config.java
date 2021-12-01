/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase.config;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;


 /**
  * <b>Title:</b>  Config
  * <br><b>Description:</b>  Config object which will obtain a properties file 
  * based on the either a Class, File or URL.  The resolution of the File and URL are
  * trivial.  The Class resolution takes the name of the class; for example
  * codebase.util.Config and appends .properties to it.  It then attempts to load the
  * resource codebase.util.config.properties as a properties file.
  * @author raymond@raykroeker.com
  * @version 1.0.0
  */
public class Config extends Properties implements Serializable {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * <b>Title:</b>  ConfigError
	 * <br><b>Description:</b>  Defines errors thrown by Config.  The possible
	 * errors are:
	 * Config file could not be found
	 * Config file could not be loaded
	 * Config file could not be stored
	 * @author raymond@raykroeker.com
	 * @version 1.0.0
	 */
	public static final class ConfigError extends Error {
		
		/** <b>Title:</b>Config Error Type<br> */
		private enum Type  {
			COULD_NOT_LOAD, COULD_NOT_STORE, FILE_NOT_FOUND
		}

		/**
		 * Create ConfigError.
		 * 
		 */
		private ConfigError() {
            super();
        }

		/**
         * Create ConfigError.
         * 
         * @param url
         *            A <code>URL</code>.
         * @param errorType
         *            A <code>Type</code>
         * @param cause
         *            A <code>Throwable</code>.
         */
		private ConfigError(final URL url, final Type type,
                final Throwable cause) {
			super(new StringBuilder(type.name())
					.append(":  ").append(url)
					.append(", ").append(cause.getMessage())
					.toString());
		}
	}

	/**
	 * The URL which represents the location of this properties object
	 */
	private URL configURL;

	/**
	 * Create Config.
	 *
	 */
	Config() {
        super();
    }

	/**
     * Create Config.
     * 
     * @param url
     *            A <code>URL</code>.
     */
	Config(URL url) {
        this(url, null);
    }

	/**
     * Create Config.
     * 
     * @param url
     *            A <code>URL</code>.
     * @param defaults
     *            A set of default <code>Properties</code>.
     */
	private Config(final URL url, final Properties defaults) {
		super(defaults);
		try {
			configURL = url;
			loadConfig();
		}
		catch(final IOException iox) {
			throw new ConfigError(configURL, ConfigError.Type.COULD_NOT_LOAD,
			        iox);
		}
	}

	/**
	 * Obtain the underlying configuration url.
	 * @return <code>java.net.URL</code>
	 */
	public URL getConfigURL() {return configURL;}
	/**
	 * Obtain a property value for a named key.
	 * @param configKey <code>ConfigKey</code>
	 * @return <code>java.lang.String</code>
	 */
	public String getProperty(ConfigKey configKey) {
		return getProperty(null == configKey ? null : configKey.toString());
	}

	/**
	 * Obtain a property value for a named key.
	 * @param configKey <code>ConfigKey</code>
	 * @param defaultValue <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 */
	public String getProperty(ConfigKey configKey, String defaultValue) {
		return getProperty(
			null == configKey ? null : configKey.toString(),
			defaultValue);
	}

	public String getFormattedProperty(final String key,
			final Object[] arguments) {
		return getFormattedProperty(key, null, arguments);
	}

	public String getFormattedProperty(final String key,
			final String defaultValue, final Object[] arguments) {
		return MessageFormat.format(getProperty(key, defaultValue), arguments);
	}

	/**
	 * Load the configuration based on the URL
	 * @throws IOException
	 */
	private void loadConfig() throws IOException {
		if(null == configURL)
			return;
		load(configURL.openStream());
	}

	/**
	 * Save the configuration represented by this object.
	 * 
	 */
	public void save(String configHeader) {
		try {
			store(
				new BufferedOutputStream(
					new FileOutputStream(configURL.getFile())),
				configHeader);
		}
		catch (final FileNotFoundException fnfx) {
			throw new ConfigError(configURL, ConfigError.Type.FILE_NOT_FOUND,
				fnfx);
		}
		catch (final IOException iox) {
			throw new ConfigError(configURL, ConfigError.Type.COULD_NOT_STORE,
				iox);
		}
	}
}
