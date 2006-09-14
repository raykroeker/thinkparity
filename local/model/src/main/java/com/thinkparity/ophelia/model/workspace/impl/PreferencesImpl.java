/*
 * Oct 17, 2005
 */
package com.thinkparity.ophelia.model.workspace.impl;

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
import com.thinkparity.ophelia.model.workspace.Preferences;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
class PreferencesImpl implements Preferences {

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

	/** The workspace implementation. */
    private final WorkspaceImpl workspace;

    /**
     * Create PreferencesImpl.
     * 
     * @param workspaceRoot
     *            The workspace root directory.
     */
	PreferencesImpl(final WorkspaceImpl workspace) {
		super();
        this.workspace = workspace;
        this.javaProperties = new Properties();
		this.javaPropertiesFile = new File(workspace.getWorkspaceDirectory(), "parity.xml");
	}

    public void clearPassword() {
        javaProperties.remove(Constants.Preferences.Properties.PASSWORD);
    }

    public File getArchiveOutputDirectory() {
        return Directories.ARCHIVE;
    }

    public Long getLastRun() {
        return getLong("lastRun");
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

    public String getServerProtocol() {
        final String override = System.getProperty("parity.serverprotcol");
        if(null != override && 0 < override.length()) { return override; }
        else { return "XMPP"; }
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

    /**
     * Obtain a long property.
     * 
     * @param name
     *            The property name.
     * @return The property value. If the value is not specified; or it is not a
     *         number; null will be returned.
     */
    Long getLong(final String name) {
        try {
            return Long.parseLong(javaProperties.getProperty(name));
        } catch (final Throwable t) {
            return null;
        }
    }

	Boolean isFirstRun() {
        final Boolean firstRun = Boolean.valueOf(
                javaProperties.getProperty("firstRun", Boolean.TRUE.toString()));
        if (firstRun) {
            javaProperties.setProperty("firstRun", Boolean.FALSE.toString());
        }
        return firstRun;
    }

	/**
	 * Load the java properties from the preferences file.
	 * 
	 * @return The java properties.
	 */
	void load() {
		try {
			initPreferences();
			javaProperties.loadFromXML(new FileInputStream(javaPropertiesFile));
		} catch (final Throwable t) {
            throw workspace.translateError(t);
		}
	}

    /**
	 * Store the java properties to the preferences file.
	 * 
	 * @param javaProperties
	 *            The java properties to store.
	 */
	void save() {
        setLastRun();
		try {
			javaProperties.storeToXML(new FileOutputStream(javaPropertiesFile), "");
        } catch (final Throwable t) {
            throw workspace.translateError(t);
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
			save();
		}
	}

    /** Set a last run timestamp in the properties. */
    private void setLastRun() {
        javaProperties.setProperty("lastRun",
                String.valueOf(System.currentTimeMillis()));
    }
}
