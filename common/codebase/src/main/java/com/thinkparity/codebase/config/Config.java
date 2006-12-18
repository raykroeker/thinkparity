/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase.config;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.StringUtil;


 /**
  * <b>Title:</b>  Config
  * <br><b>Description:</b>  Config object which will obtain a properties file 
  * based on the either a Class, File or URL.  The resolution of the File and URL are
  * trivial.  The Class resolution takes the name of the class; for example
  * codebase.util.Config and appends .properties to it.  It then attempts to load the
  * resource codebase.util.config.properties as a properties file.
  * @author raykroeker@gmail.com
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
	 * @author raykroeker@gmail.com
	 * @version 1.0.0
	 */
	public static final class ConfigError extends Error {
		private static final long serialVersionUID = 1;
		
		/**
		 * <b>Title:</b>  ErrorType
		 * <br><b>Description:</b>  The actual error message definitions thrown by
		 * Config.
		 * @author raykroeker@gmail.com
		 * @version 1.0.1
		 */
		public static final class ErrorType extends com.thinkparity.codebase.Enum {
			private static final long serialVersionUID = 1;
			
			/**
			 * Config file could not be loaded due to unknown IOException
			 */
			private static final ErrorType CouldNotLoad =
				new ErrorType("Config file specified could not be loaded.");

			/**
			 * Config file could not be stored due to unknown IOException
			 */
			private static final ErrorType CouldNotStore =
				new ErrorType("Config file specified could not be stored.");
		
			/**
			 * Config file does not exist in the location specified
			 */
			private static final ErrorType FileNotFound =
				new ErrorType("Config file specified could not be found.");

			/**
			 * Create a new ErrorType
			 * @param errorType <code>java.lang.String</code>
			 */
			private ErrorType(String errorType) {super(errorType);}
		}

		/**
		 * Create a new ConfigError
		 */
		private ConfigError() {super();}

		/**
		 * Create a new ConfigError
		 * @param url <code>java.net.URL</code>
		 * @param errorType <code>Config$ErrorType</code>
		 * @param cause <code>java.lang.Throwable</code>
		 */
		private ConfigError(URL url, ErrorType errorType, Throwable cause) {
			super(
				new StringBuffer()
					.append(errorType)
					.append(":  ")
					.append(url)
					.append(", ")
					.append(cause.getMessage())
					.toString());
		}
	}

	/**
	 * The URL which represents the location of this properties object
	 */
	private URL configURL;

	/**
	 * Create a new Config
	 */
	Config() {super();}

	/**
	 * Create a new Config
	 * @param clasz <code>java.lang.Class</code>
	 */
	Config(Class clasz) {this(clasz, null);}

	/**
	 * Create a new Config
	 * @param clasz <code>java.lang.Class</code>
	 * @param defaults <code>java.util.Properties</code>
	 */
	Config(Class clasz, Properties defaults) {
		super(defaults);
		try {
			if(null == clasz)
				configURL = null;
			else
				configURL = resolveUrl(clasz);
			loadConfig();
		}
		catch (FileNotFoundException fnfx) {
			throw new ConfigError(
				configURL,
				ConfigError.ErrorType.FileNotFound,
				fnfx);
		}
		catch (IOException iox) {
			throw new ConfigError(
				configURL,
				ConfigError.ErrorType.CouldNotLoad,
				iox);
		}
	}

	/**
	 * Create a new Config
	 * @param file <code>java.io.File</code>
	 */	
	Config(File file) {this(file, null);}

	/**
     * Create a new Config
     * 
     * @param file
     *            <code>java.io.File</code>
     * @param defaults
     *            <code>java.util.Properties</code>
     */	
	Config(final File file, final Properties defaults) {
		super(defaults);
		try {
			if(null == file)
				configURL = null;
			else
				configURL = file.toURI().toURL();
			loadConfig();
		} catch (final FileNotFoundException fnfx) {
			throw new ConfigError(
				configURL,
				ConfigError.ErrorType.FileNotFound,
				fnfx);
		} catch (IOException iox) {
			throw new ConfigError(
				configURL,
				ConfigError.ErrorType.CouldNotLoad,
				iox);
		}
	}

	/**
	 * Create a new Config
	 * @param defaults <code>java.util.Properties</code>
	 */
	Config(Properties defaults) {this((Class) null, defaults);}
	
	/**
	 * Create a new Config
	 * @param url <code>java.net.URL</code>
	 */
	Config(URL url) {this(url, null);}

	/**
	 * Create a new Config
	 * @param url <code>java.net.URL</code>
	 * @param defaults <code>java.util.Properties</code>
	 */
	Config(URL url, Properties defaults) {
		super(defaults);
		try {
			configURL = url;
			loadConfig();
		}
		catch(IOException iox) {
			throw new ConfigError(
				configURL,
				ConfigError.ErrorType.CouldNotLoad,
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
	 * Build a URL to identify the properties object, using clasz as input.
	 * @param clasz <code>java.lang.Class</code>
	 * @return <code>java.net.URL</code>
	 */
	private URL resolveUrl(Class clasz) {
		// codebase.util.Config
		final String qualifiedClaszName = clasz.getName();
		// Config
		final String claszName = qualifiedClaszName.substring(
			qualifiedClaszName.lastIndexOf(".") + 1);
		// codebase.util.config
		final String claszUrl = clasz.getPackage().getName() + "." +
			claszName.substring(0, 1).toLowerCase() +
			claszName.substring(1);
		// codebase.util.config.properties
		return ResourceUtil.getURL(
			StringUtil.searchAndReplace(claszUrl, ".", "/") 
			.append(".properties").toString());
	}

	/**
	 * Save the configuration represented by this object.
	 */
	public void save(String configHeader) {
		try {
			store(
				new BufferedOutputStream(
					new FileOutputStream(configURL.getFile())),
				configHeader);
		}
		catch (FileNotFoundException fnfx) {
			throw new ConfigError(
				configURL,
				ConfigError.ErrorType.FileNotFound,
				fnfx);
		}
		catch (IOException iox) {
			throw new ConfigError(
				configURL,
				ConfigError.ErrorType.CouldNotStore,
				iox);
		}
	}
}
