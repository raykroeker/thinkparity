/*
 * Oct 17, 2005
 */
package com.thinkparity.model.parity.model.workspace;

import java.util.Locale;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
class PreferencesHelper {

	/**
	 * Create a PreferencesHelper.
	 */
	PreferencesHelper() { super(); }

	/**
	 * Obtain a preferences interface for the parity workspace. This interface
	 * allows the user to get\set specific preferences that will be persisted
	 * between sessions.
	 * 
	 * @return The interface with which the client can interact.
	 */
	Preferences getPreferences() {
		final java.util.prefs.Preferences javaPrefs = initJavaPrefs();

		return new Preferences() {
			/**
			 * @see com.thinkparity.model.parity.model.workspace.Preferences#getLocale()
			 */
			public Locale getLocale() { return Locale.getDefault(); }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Preferences#getServerHost()
			 */
			public String getServerHost() {
				final String override = System.getProperty("parity.serverhost");
				if(null != override && 0 < override.length()) { return override; }
				else { return "thinkparity.dyndns.org"; }
			}

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Preferences#getServerPort()
			 */
			public Integer getServerPort() {
				final Integer override = Integer.getInteger("parity.serverport");
				if(null != override) { return override; }
				else { return 5223; }
			}

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Preferences#getUsername()
			 */
			public String getUsername() {
				final String username = javaPrefs.get("parity.username", null);
				if(null != username) { return username; }
				else { return "system"; }
			}

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Preferences#isSetLocale()
			 */
			public Boolean isSetLocale() { return Boolean.TRUE; }

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Preferences#isSetUsername()
			 */
			public Boolean isSetUsername() {
				final String username = getUsername();
				return (null != username && 0 < username.length());
			}

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Preferences#setLocale(java.util.Locale)
			 */
			public void setLocale(Locale locale) {
				// TODO Auto-generated method stub
			}

			/**
			 * @see com.thinkparity.model.parity.model.workspace.Preferences#setUsername(java.lang.String)
			 */
			public void setUsername(final String username) {
				javaPrefs.put("parity.username", username);
			}
		};
	}

	/**
	 * Initialize the java preferences object. This will initialize the
	 * preferences file (creating it if it does not exist); then import those
	 * preferences into the user root preferences object. The user root
	 * preferences object is then returned.
	 * 
	 * @return The preferences for 
	 */
	private java.util.prefs.Preferences initJavaPrefs() {
		try { return java.util.prefs.Preferences.userRoot().node("Parity Software/Parity"); }
		catch(Throwable t) {
			t.printStackTrace();
			return null;
		}
	}
}
