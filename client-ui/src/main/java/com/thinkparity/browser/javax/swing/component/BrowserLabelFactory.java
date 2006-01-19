/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.javax.swing.component;

import javax.swing.JLabel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserLabelFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final BrowserLabelFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new BrowserLabelFactory();
		singletonLock = new Object();
	}

	public static JLabel create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}
	public static JLabel create(final String text) {
		synchronized(singletonLock) { return singleton.doCreate(text); }
	}

	/**
	 * Create a BrowserButtonFactory.
	 */
	private BrowserLabelFactory() { super(); }

	private JLabel doCreate() { return doCreate(""); }

	private JLabel doCreate(final String text) {
		// draw the button as a bottom button.
		final JLabel jLabel = new JLabel(text);
		return jLabel;		
	}
}
