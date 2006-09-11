/*
 * Jan 30, 2006
 */
package com.thinkparity.codebase.swing;

import javax.swing.JOptionPane;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JOptionPaneUtil {

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
