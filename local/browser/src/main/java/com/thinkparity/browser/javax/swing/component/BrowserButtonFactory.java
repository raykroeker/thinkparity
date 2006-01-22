/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.javax.swing.component;

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

	public static JButton create(final String text) { 
		synchronized(singletonLock) { return singleton.doCreate(text); }
	}

	public static JButton createTop(final String text) {
		synchronized(singletonLock) { return singleton.doCreateTop(text); }
	}

	/**
	 * Create a JButton. The default button will be drawn as a "bottom" button.
	 * 
	 * @return The JButton.
	 */
	public static JButton createBottom() {
		synchronized(singletonLock) { return singleton.doCreateBottom(); }
	}

	/**
	 * Create a JButton with the specified text.
	 * 
	 * @param text
	 *            The JButton text.
	 * @return The JButton.
	 */
	public static JButton createBottom(final String text) {
		synchronized(singletonLock) { return singleton.doCreateBottom(text); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private BrowserButtonFactory() { super(); }

	/**
	 * Create a JButton. The default button will be drawn as a "bottom" button.
	 * 
	 * @return The JButton.
	 */
	private JButton doCreateBottom() { return doCreateBottom(""); }

	/**
	 * Create a JButton with the specified text.
	 * 
	 * @param text
	 *            The JButton text.
	 * @return The JButton.
	 */
	private JButton doCreateBottom(final String text) {
		return doCreate(text, SubstanceConstants.Side.BOTTOM);
	}

	private JButton doCreateTop(final String text) {
		return doCreate(text, SubstanceConstants.Side.TOP);
	}

	private JButton doCreate(final String text,
			final SubstanceConstants.Side side) {
		// draw the button as a bottom button.
		final JButton jButton = new JButton(text);
		jButton.putClientProperty(
				SubstanceLookAndFeel.BUTTON_SIDE_PROPERTY,
				side.name());
		return jButton;		
	}

	private JButton doCreate(final String text) {
		final JButton jButton = new JButton(text);
		jButton.putClientProperty(
				SubstanceLookAndFeel.BUTTON_SHAPER_PROPERTY,
				"org.jvnet.substance.button.ClassicButtonShaper");
		return jButton;
	}
}
