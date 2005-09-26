/*
 * Feb 12, 2005
 */
package com.thinkparity.model.parity.util.prefs;

/**
 * ParityPrefsKey
 * An enum which defines all of the keys for use with ParityPrefs in
 * storing\retrieving preferences.  The ParityPrefsKey also defines a default
 * value for use in <i>most</i> situations.
 * @author raykroeker@gmail.com
 * @version 1.4
 */
public enum ParityPrefsKey {

	/**
	 * ClientPrefsKeys
	 */
	BrowserHeight("BrowserHeight", "650"),
	BrowserWidth("BrowserWidth", "800"),
	BrowserX("BrowserX", "-1"),
	BrowserY("BrowserY", "-1"),
	Country("Country", null),
	DocumentInfoPaneRememberedAnswer("DocumentInfoPaneRememberedAnswer", null),
	HomeDirectoryAbsolutePath("HomeDirectoryAbsolutePath", null),
	Host("Host", "raykroeker.dyndns.org"),
	Language("Language", null),
	Port("Port", "5222"),
	TimeZone("TimeZone", null),
	Username("Username", null),
	Version("Version", null);

	/**
	 * Contains the default value for the key.
	 */
	private String defaultValue;

	/**
	 * Underlying key used by ParityPrefs to store preferences.
	 */
	private String key;

	/**
	 * Create a ParityPrefsKey [Enum]
	 * @param key <code>String</code>
	 */
	ParityPrefsKey(String key, String defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	/**
	 * Obtain the default value for a preference.
	 * @return <code>String</code>
	 */
	public String defaultValue() { return defaultValue; }

	/**
	 * Obtain the enum's key.
	 * @return <code>String</code>
	 */
	String key() { return key; }
}
