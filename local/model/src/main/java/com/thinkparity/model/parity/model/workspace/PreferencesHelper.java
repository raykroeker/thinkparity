/*
 * Oct 17, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
class PreferencesHelper {

	/**
	 * The java properties file to read\write.
	 */
	private final File preferencesFile;

	/**
	 * Create a PreferencesHelper.
	 */
	PreferencesHelper(final File workspaceRoot) {
		super();
		this.preferencesFile = new File(workspaceRoot, "parity.xml");
	}

	/**
	 * Obtain a preferences interface for the parity workspace. This interface
	 * allows the user to get\set specific preferences that will be persisted
	 * between sessions.
	 * 
	 * @return The interface with which the client can interact.
	 */
	Preferences getPreferences() {
		final Properties javaProperties = loadPreferences();
		// save the preferences on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() { storePreferences(javaProperties); }
		});

		return new Preferences() {
			public Locale getLocale() { return Locale.getDefault(); }
			public String getServerHost() {
				final String override = System.getProperty("parity.serverhost");
				if(null != override && 0 < override.length()) { return override; }
				else { return "thinkparity.dyndns.org"; }
			}
			public Integer getServerPort() {
				final Integer override = Integer.getInteger("parity.serverport");
				if(null != override) { return override; }
				else {
					if(Boolean.getBoolean("parity.insecure")) { return 5222; }
					else { return 5223; }
				}
			}
			public String getSystemUsername() { return "system"; }
			public String getUsername() {
				return javaProperties.getProperty("parity.username", null);
			}
			public Boolean isSetLocale() { return Boolean.TRUE; }
			public Boolean isSetUsername() {
				final String username = getUsername();
				return (null != username && 0 < username.length());
			}
			public void setLocale(Locale locale) {
				// TODO Auto-generated method stub
			}
			public void setUsername(final String username) {
				javaProperties.setProperty("parity.username", username);
			}
		};
	}

	/**
	 * Load the java properties from the preferences file.
	 * 
	 * @return The java properties.
	 */
	private Properties loadPreferences() {
		try {
			initPreferencesFile();
			final Properties javaProperties = new Properties();
			javaProperties.loadFromXML(new FileInputStream(preferencesFile));
			return javaProperties;
		}
		catch(FileNotFoundException fnfx) {
			fnfx.printStackTrace(System.err);
			return null;
		}
		catch(IOException iox) {
			iox.printStackTrace(System.err);
			return null;
		}
	}

	/**
	 * Initialize the preferences file by creating it if it does not already
	 * exist.
	 * 
	 * @throws IOException
	 */
	private void initPreferencesFile() throws IOException {
		if(!preferencesFile.exists())
			Assert.assertTrue("init", preferencesFile.createNewFile());
	}

	/**
	 * Store the java properties to the preferences file.
	 * 
	 * @param javaProperties
	 *            The java properties to store.
	 */
	private void storePreferences(final Properties javaProperties) {
		try {
			javaProperties.storeToXML(new FileOutputStream(preferencesFile), "");
		}
		catch(FileNotFoundException fnfx) {
			fnfx.printStackTrace(System.err);
		}
		catch(IOException iox) {
			iox.printStackTrace(System.err);
		}
	}
}
