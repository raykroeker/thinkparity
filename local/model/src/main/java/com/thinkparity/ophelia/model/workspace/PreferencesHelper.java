/*
 * Oct 17, 2005
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Constants;
import com.thinkparity.ophelia.model.Constants.Directories;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
class PreferencesHelper {

	/**
	 * Assertion message for setting the password a second time.
	 */
	private static final String ASSERT_NOT_IS_SET_PASSWORD = new StringBuffer()
		.append("The parity password cannot be modified after it has ")
		.append("initially been set.").toString();

	/**
	 * Assertion message for setting the username a second time.
	 */
	private static final String ASSERT_NOT_IS_SET_USERNAME = new StringBuffer()
		.append("The parity username cannot be modified after it has ")
		.append("initially been set.").toString();

	/** The java properties object. */
    private final Properties javaProperties;

    /** The java properties xml file to read\write. */
	private final File javaPropertiesFile;

	/**
     * Create PreferencesHelper.
     * 
     * @param workspaceRoot
     *            The workspace root directory.
     */
	PreferencesHelper(final File workspaceRoot) {
		super();
        this.javaProperties = new Properties();
		this.javaPropertiesFile = new File(workspaceRoot, "parity.xml");
	}

	/**
     * Obtain a preferences interface for the parity workspace. This interface
     * allows the user to get\set specific preferences that will be persisted
     * between sessions.
     * 
     * @return The interface with which the client can interact.
     */
	Preferences getPreferences() {
	    loadPreferences();

		return new Preferences() {

			public void clearPassword() {
				javaProperties.remove(Constants.Preferences.Properties.PASSWORD);
			}

			public File getArchiveOutputDirectory() { 
                return Directories.ARCHIVE;
			}

            public Long getLastRun() {
                final String lastRun = javaProperties.getProperty(Constants.Preferences.Properties.LAST_RUN);
                if(null == lastRun) { return null; }
                else {
                    try { return Long.parseLong(lastRun); }
                    catch(final NumberFormatException nfx) { throw new RuntimeException(nfx); }
                }
            }

			public Locale getLocale() { return Locale.getDefault(); }

			public String getPassword() {
				return javaProperties.getProperty("parity.password");
			}

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

			public User getSystemUser() { return User.THINK_PARITY; }

			public String getUsername() {
				return javaProperties.getProperty("parity.username", null);
			}

			public Boolean isSetArchiveOutputDirectory() {
				return null != getArchiveOutputDirectory();
			}

			public Boolean isSetLocale() { return Boolean.TRUE; }

			public Boolean isSetPassword() {
				final String password = getPassword();
				return (null != password && 0 < password.length());
			}

			public Boolean isSetUsername() {
				final String username = getUsername();
				return (null != username && 0 < username.length());
			}

			public void setLocale(final Locale locale) {}

			public void setPassword(final String password) {
				Assert.assertNotTrue(ASSERT_NOT_IS_SET_PASSWORD, isSetPassword());
				javaProperties.setProperty("parity.password", password);
			}

			public void setUsername(final String username) {
				Assert.assertNotTrue(ASSERT_NOT_IS_SET_USERNAME, isSetUsername());
				javaProperties.setProperty("parity.username", username);
			}
		};
	}

	/**
	 * Store the java properties to the preferences file.
	 * 
	 * @param javaProperties
	 *            The java properties to store.
	 */
	void savePreferences() {
        setLastRun();
		try {
			javaProperties.storeToXML(new FileOutputStream(javaPropertiesFile), "");
		}
		catch(final IOException iox) {
            throw WorkspaceException.translate(
                    "[LMODEL] [WORKSPACE] [PREFS] [STORE PREFS] [IO ERROR]", iox);
		}
	}

	/**
	 * Initialize the preferences file by creating it if it does not already
	 * exist.
	 * 
	 * @throws IOException
	 */
	private void initPreferences() throws IOException {
		if(!javaPropertiesFile.exists()) {
			Assert.assertTrue(
                    "[LMODEL] [WORKSPACE] [INIT PREFS] [CANNOT CREATE PREFS FILE]",
                    javaPropertiesFile.createNewFile());
			savePreferences();
		}
	}

	/**
	 * Load the java properties from the preferences file.
	 * 
	 * @return The java properties.
	 */
	private void loadPreferences() {
		try {
			initPreferences();
			javaProperties.loadFromXML(new FileInputStream(javaPropertiesFile));
		}
		catch(final IOException iox) {
            throw WorkspaceException.translate(
                    "[LMODEL] [WORKSPACE] [PREFS] [LOAD PREFS] [IO ERROR]", iox);
		}
	}

    /** Set a last run timestamp in the properties. */
    private void setLastRun() {
        javaProperties.setProperty(
            Constants.Preferences.Properties.LAST_RUN,
            String.valueOf(System.currentTimeMillis()));
    }
}
