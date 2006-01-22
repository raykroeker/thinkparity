/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.ui.component;

import java.awt.Font;

import javax.swing.JLabel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LabelFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final LabelFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new LabelFactory();
		singletonLock = new Object();
	}

	public static JLabel create(final Font font) {
		synchronized(singletonLock) { return singleton.doCreate(font); }
	}

	public static JLabel create(final Font font, final String text) {
		synchronized(singletonLock) { return singleton.doCreate(font, text); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private LabelFactory() { super(); }

	private JLabel doCreate(final Font font) { return doCreate(font, ""); }

	private JLabel doCreate(final Font font, final String text) {
		// draw the button as a bottom button.
		final JLabel jLabel = new JLabel(text);
		jLabel.setFont(font);
		return jLabel;		
	}
}
