/*
 * Jan 30, 2006
 */
package com.thinkparity.codebase.swing;

import javax.swing.JOptionPane;

import com.thinkparity.browser.Version;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JOptionPaneUtil {

	private static final String DEFAULT_TITLE;

	static { DEFAULT_TITLE = Version.getName(); }

	public static Boolean showConfirmationDialog(final Object message) {
		return showConfirmationDialog(message, DEFAULT_TITLE);
	}

	public static Boolean showConfirmationDialog(final Object message,
			final String title) {
		if(JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
				message, title, JOptionPane.YES_NO_OPTION)) {
			return Boolean.TRUE;
		}
		else { return Boolean.FALSE; }
	}

	/**
	 * Create a JOptionPaneUtil [Singleton]
	 * 
	 */
	private JOptionPaneUtil() { super(); }
}
