/*
 * Feb 4, 2005
 */
package com.thinkparity.model.parity.util.prefs;

import java.io.File;
import java.util.Locale;
import java.util.TimeZone;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.util.ParityUtil;
import com.thinkparity.model.parity.util.Point2D;

/**
 * ParityPrefs
 * Preferences used by the client application.  In a win32 environment, the
 * preferences are stored in the registry under the following key:
 * HKEY_CURRENT_USER\Software\JavaSoft\Prefs
 * followed by a package specification.
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public class ParityPrefs {

	/**
	 * Handle to the system preferences.
	 */
	private static Preferences systemPreferences;

	/**
	 * Handle to the user preferences.
	 */
	private static Preferences userPreferences;

	static {
		// create the user preferences
		userPreferences =
			Preferences.userNodeForPackage(ParityUtil.getClientClass());
		// create the system preferences
		systemPreferences =
			Preferences.systemNodeForPackage(ParityUtil.getClientClass());
	}

	/**
	 * Clear all of the system preferences.
	 */
	private static void system_Clear() throws BackingStoreException {
		systemPreferences.clear();
	}

	/**
	 * Obtain a system preference as a String.  The default value is encapsulated
	 * within the ParityPrefsKey object itself.
	 * @param key <code>org.kcs.projectmanager.util.prefs.ClientPrefsKey</code>
	 */
	private static String system_Get(ParityPrefsKey key) {
		return systemPreferences.get(key.key(), key.defaultValue());
	}

	/**
	 * Set a system preference.
	 * @param key <code>org.kcs.projectmanager.util.prefs.ClientPrefsKey</code>
	 * @param value <code>String</code>
	 */
	private static void system_Put(ParityPrefsKey key, String value) {
		systemPreferences.put(key.key(), value);
	}

	/**
	 * Unset a system preference.
	 * @param key <code>org.kcs.projectmanager.util.prefs.ClientPrefsKey</code>
	 */
	private static void system_Remove(ParityPrefsKey key) {
		systemPreferences.remove(key.key());
	}

	/**
	 * Clear all of the user preferences.
	 */
	private static void user_Clear() throws BackingStoreException {
		userPreferences.clear();
	}

	/**
	 * Obtain a user preference as a String.  The default value is encapsulated
	 * within the ParityPrefsKey object itself.
	 * @see java.util.prefs.Preferences#get(java.lang.String, java.lang.String)
	 * @param key <code>org.kcs.projectmanager.util.prefs.ClientPrefsKey</code>
	 * @return <code>String</code>
	 */
	private static String user_Get(ParityPrefsKey key) {
		return userPreferences.get(key.key(), key.defaultValue());
	}

	/**
	 * Obtain a user preference as a Boolean value.  The default value is
	 * encapsulated within the ParityPrefsKey object itself.
	 * @param key <code>org.kcs.projectmanager.util.prefs.ClientPrefsKey</code>
	 * @return <code>String</code>
	 */
	private static Boolean user_GetBoolean(ParityPrefsKey key) {
		return userPreferences.getBoolean(key.key(), Boolean.parseBoolean(key
				.defaultValue()));
	}

	/**
	 * Obtain a user preference as an Integer.  The default value is encapsulated
	 * within the ParityPrefsKey object itself.
	 * @see java.util.prefs.Preferences#getInt(java.lang.String, int)
	 * @param key <code>org.kcs.projectmanager.util.prefs.ClientPrefsKey</code>
	 * @return <code>Integer</code>
	 */
	private static Integer user_GetInt(ParityPrefsKey key) {
		return userPreferences.getInt(key.key(), Integer.parseInt(key
				.defaultValue()));
	}

	/**
	 * Save a user preference.
	 * @param key <code>org.kcs.projectmanager.util.prefs.ClientPrefsKey</code>
	 * @param value <code>String</code>
	 */
	private static void user_Put(ParityPrefsKey key, String value) {
		userPreferences.put(key.key(), value);
	}

	/**
	 * Save an integer user preference.
	 * @param key <code>String</code>
	 * @param value <code>Integer</code>
	 */
	private static void user_PutInt(ParityPrefsKey key, Integer value) {
		userPreferences.putInt(key.key(), value);
	}

	/**
	 * Remove a user preference specified by key.
	 * @param key <code>org.kcs.projectmanager.util.prefs.ClientPrefsKey</code>
	 */
	private static void user_Remove(ParityPrefsKey key) {
		userPreferences.remove(key.key());
	}

	/**
	 * Remove all client preferences.
	 */
	public static void clear() throws BackingStoreException {
		ParityPrefs.system_Clear();
		ParityPrefs.user_Clear();
	}

	/**
	 * Obtain the absolute path for the home directory for the client
	 * application.
	 * 
	 * @return <code>java.lang.String</code>
	 */
	public static String getHomeDirectoryAbsolutePath() {
		final String preferencesOverride = System.getProperty("parity.workspace");
		if(null != preferencesOverride && 0 < preferencesOverride.length())
			return preferencesOverride;
		return ParityPrefs.user_Get(ParityPrefsKey.HomeDirectoryAbsolutePath);
	}

	/**
	 * Get the host to login to.
	 * @return <code>String</code>
	 */
	public static String getHost() {
		final String preferencesOverride = System.getProperty("parity.host");
		if(null != preferencesOverride && 0 < preferencesOverride.length())
			return preferencesOverride;
		return ParityPrefs.user_Get(ParityPrefsKey.Host);
	}

	/**
	 * Obtain the locale for the user.  If the locale is not set, the default
	 * system locale is used, and saved.
	 * @return <code>java.util.Locale</code>
	 */
	public static Locale getLocale() {
		final String country = ParityPrefs.user_Get(ParityPrefsKey.Country);
		final String language = ParityPrefs.user_Get(ParityPrefsKey.Language);
		Locale locale;
		if(null == country || null == language) {
			locale = Locale.getDefault();
			setLocale(locale);
		}
		else { locale = new Locale(country, language); }
		return locale;
	}

	/**
	 * Get the port to login to.
	 * @return <code>Integer</code>
	 */
	public static Integer getPort() {
		return ParityPrefs.user_GetInt(ParityPrefsKey.Port);
	}

	/**
	 * Obtain the timezone for the user.  If the timezone is not specified, the
	 * default is saved, then returned.
	 * @return <code>java.util.TimeZone</code>
	 */
	public static TimeZone getTimeZone() {
		final String timeZoneID = ParityPrefs.user_Get(ParityPrefsKey.TimeZone);
		TimeZone timeZone;
		if(null == timeZoneID) {
			timeZone = TimeZone.getDefault();
			setTimeZone(timeZone);
		}
		else { timeZone = TimeZone.getTimeZone(timeZoneID); }
		return timeZone;
	}

	/**
	 * Obtain the username for the user.
	 * @return <code>java.lang.String</code>
	 * @deprecated
	 */
	public static String getUsername() {
		final String preferencesOverride = System.getProperty("parity.username");
		if(null != preferencesOverride && 0 < preferencesOverride.length())
			return preferencesOverride;
		return ParityPrefs.user_Get(ParityPrefsKey.Username);
	}

	/**
	 * Obtain the currently installed version.
	 * @return <code>String</code>
	 */
	public static String getVersion() {
		return ParityPrefs.user_Get(ParityPrefsKey.Version);
	}

	/**
	 * Remove the home directory preference.
	 */
	public static void removeHomeDirectory() {
		ParityPrefs.user_Remove(ParityPrefsKey.HomeDirectoryAbsolutePath);
	}

	/**
	 * Remove the host directory preference.
	 */
	public static void removeHost() {
		ParityPrefs.user_Remove(ParityPrefsKey.Host);
	}

	/**
	 * Remove the port preference.
	 */
	public static void removePort() {
		ParityPrefs.user_Remove(ParityPrefsKey.Port);
	}

	/**
	 * Unset the current version preference.
	 */
	public static void removeVersion() {
		ParityPrefs.user_Remove(ParityPrefsKey.Version);
	}

	/**
	 * Set the home directory saved in the user preferences.
	 * 
	 * @param homeDirectoryAbsolutePath
	 *            The absolute path of the home directory.
	 */
	public static void setHomeDirectoryAbsolutePath(
			final String homeDirectoryAbsolutePath) {
		Assert.assertNotNull(
				"The home directory's absolute path cannot be set to null.",
				homeDirectoryAbsolutePath);
		final File homeDirectory = new File(homeDirectoryAbsolutePath);
		Assert.assertTrue("The home directory's absolute path must exist.",
				homeDirectory.exists());
		Assert.assertTrue("The home directory's absolute path must be a directory.",
				homeDirectory.isDirectory());
		Assert.assertTrue("The home directory's absolue path must be readable.",
				homeDirectory.canRead());
		Assert.assertTrue("The home directory's absolute path must be writeable.",
				homeDirectory.canWrite());
		ParityPrefs.user_Put(ParityPrefsKey.HomeDirectoryAbsolutePath,
				homeDirectoryAbsolutePath);
	}

	/**
	 * Set the host within the user preferences.
	 * @param host <code>String</code>
	 */
	public static void setHost(String host) {
		ParityPrefs.user_Put(ParityPrefsKey.Host, host);
	}

	/**
	 * Set the locale for the current user.
	 * @param locale <code>java.util.Locale</code>
	 */
	public static void setLocale(final Locale locale) {
		final String country = locale.getCountry();
		final String language = locale.getLanguage();
		ParityPrefs.user_Put(ParityPrefsKey.Country, country);
		ParityPrefs.user_Put(ParityPrefsKey.Language, language);
	}

	/**
	 * Set the port within the user preferences.
	 * @param port <code>Integer</code>
	 */
	public static void setPort(Integer port) {
		ParityPrefs.user_PutInt(ParityPrefsKey.Port, port);
	}

	/**
	 * Save the time zone within the user preferences.
	 * @param timeZone <code>java.util.TimeZone</code>
	 */
	public static void setTimeZone(final TimeZone timeZone) {
		final String timeZoneID = timeZone.getID();
		ParityPrefs.user_Put(ParityPrefsKey.TimeZone, timeZoneID);
	}

	/**
	 * Set the username.
	 * @param username <code>java.lang.String</code>
	 */
	public static void setUsername(final String username) {
		ParityPrefs.user_Put(ParityPrefsKey.Username, username);
	}

	/**
	 * Set the current version of the client application in the
	 * user preferences.
	 */
	public static void setVersion() {
		ParityPrefs.user_Put(ParityPrefsKey.Version, ParityUtil.getVersion());
	}

	public static void setBrowserSize(final Point2D browserSize) {
		ParityPrefs.user_PutInt(ParityPrefsKey.BrowserHeight, browserSize.getY());
		ParityPrefs.user_PutInt(ParityPrefsKey.BrowserWidth, browserSize.getX());
	}

	public static Point2D getBrowserSize() {
		return new Point2D(
				ParityPrefs.user_GetInt(ParityPrefsKey.BrowserWidth),
				ParityPrefs.user_GetInt(ParityPrefsKey.BrowserHeight));
	}

	public static void setBrowserLocation(final Point2D browserLocation) {
		ParityPrefs.user_PutInt(ParityPrefsKey.BrowserX, browserLocation.getX());
		ParityPrefs.user_PutInt(ParityPrefsKey.BrowserY, browserLocation.getY());
	}
	
	public static Point2D getBrowserLocation() {
		final Integer browserX = ParityPrefs.user_GetInt(ParityPrefsKey.BrowserX);
		final Integer browserY = ParityPrefs.user_GetInt(ParityPrefsKey.BrowserY);
		if(0 > browserX || 0 > browserY)
			return null;
		return new Point2D(browserX, browserY);
	}

	public static String getDocumentInfoPaneRememberedAnswer() {
		return ParityPrefs.user_Get(ParityPrefsKey.DocumentInfoPaneRememberedAnswer);
	}

	public static void removeDocumentInfoPaneRememberedAnswer() {
		ParityPrefs.user_Remove(ParityPrefsKey.DocumentInfoPaneRememberedAnswer);
	}

	public static void setDocumentInfoPaneRememberedAnswer(
			final String documentInfoPaneRememberedAnswer) {
		ParityPrefs.user_Put(ParityPrefsKey.DocumentInfoPaneRememberedAnswer,
				documentInfoPaneRememberedAnswer);
	}

	/**
	 * Create a ParityPrefs [Singleton]
	 */
	private ParityPrefs() { super(); }
}
