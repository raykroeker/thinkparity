/*
 * Jan 1, 2006
 */
package com.thinkparity.browser.javax.swing;

import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.jvnet.substance.theme.SubstanceTheme;

import com.thinkparity.browser.javax.swing.plaf.parity.ParityLookAndFeel;
import com.thinkparity.browser.javax.swing.plaf.parity.theme.ParityTheme;
import com.thinkparity.browser.log4j.LoggerFactory;

/**
 * A wrapper around the UIManager as well as the substance l & f plugin.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserUI {

	/**
	 * Handle to an apache logger.
	 */
	protected static final Logger slogger;

	/**
	 * Handle to the parity look and feel.
	 */
	private static LookAndFeel parityLookAndFeel;

	static {
		slogger = LoggerFactory.getLogger(BrowserUI.class);

		JFrame.setDefaultLookAndFeelDecorated(true);
		System.setProperty("sun.awt.noerasebackground", "true");
	}

	/**
	 * Set the substance look and feel.
	 *
	 */
	static void setParityLookAndFeel() {
		if(null == parityLookAndFeel) {
			parityLookAndFeel = new ParityLookAndFeel(new ParityTheme());
		}
		try { setLookAndFeel(parityLookAndFeel); }
		catch(UnsupportedLookAndFeelException ulafx) {
			slogger.error("Could set substance look and feel.", ulafx);
		}
	}

	static void setCurrentTheme(final SubstanceTheme substanceTheme) {
//		parityLookAndFeel = new ParityLookAndFeel(substanceTheme);
//		setParityLookAndFeel();
	}

	/**
	 * Set the look and feel via the UI Manager.
	 * 
	 * @param newLookAndFeel
	 *            The new look and feel.
	 * @throws UnsupportedLookAndFeelException
	 */
	private static void setLookAndFeel(final LookAndFeel newLookAndFeel)
			throws UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(newLookAndFeel);
	}

	/**
	 * Create a BrowserUI [Singleton]
	 */
	private BrowserUI() { super(); }
}
