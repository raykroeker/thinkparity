/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.ui.component;

import javax.swing.JButton;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ButtonFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final ButtonFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new ButtonFactory();
		singletonLock = new Object();
	}

	public static JButton create(final String text) { 
		synchronized(singletonLock) { return singleton.doCreate(text); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private ButtonFactory() { super(); }

	private JButton doCreate(final String text) {
		final JButton jButton = new JButton(text);
		return jButton;		
	}
}
