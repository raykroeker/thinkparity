/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.javax.swing.component;

import javax.swing.JCheckBox;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserCheckBoxFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final BrowserCheckBoxFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new BrowserCheckBoxFactory();
		singletonLock = new Object();
	}

	public static JCheckBox create(final String text) {
		synchronized(singletonLock) { return singleton.doCreate(text); }
	}

	/**
	 * Create a BrowserButtonFactory.
	 */
	private BrowserCheckBoxFactory() { super(); }

	private JCheckBox doCreate(final String text) {
		final JCheckBox jCheckBox = new JCheckBox(text);
		return jCheckBox;		
	}
}
