/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.javax.swing.button;

import javax.swing.JButton;

import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.utils.SubstanceConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserButtonFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final BrowserButtonFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new BrowserButtonFactory();
		singletonLock = new Object();
	}

	/**
	 * Create a JButton. The default button will be drawn as a "bottom" button.
	 * 
	 * @return The JButton.
	 */
	public static JButton create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	/**
	 * Create a JButton with the specified text.
	 * 
	 * @param text
	 *            The JButton text.
	 * @return The JButton.
	 */
	public static JButton create(final String text) {
		synchronized(singletonLock) { return singleton.doCreate(text); }
	}

	/**
	 * Create a BrowserButtonFactory.
	 */
	private BrowserButtonFactory() { super(); }

	/**
	 * Create a JButton. The default button will be drawn as a "bottom" button.
	 * 
	 * @return The JButton.
	 */
	private JButton doCreate() { return doCreate(""); }

	/**
	 * Create a JButton with the specified text.
	 * 
	 * @param text
	 *            The JButton text.
	 * @return The JButton.
	 */
	private JButton doCreate(final String text) {
		// draw the button as a bottom button.
		final JButton jButton = new JButton(text);
		jButton.putClientProperty(
				SubstanceLookAndFeel.BUTTON_SIDE_PROPERTY,
				SubstanceConstants.Side.BOTTOM.name());
		return jButton;
	}
}
