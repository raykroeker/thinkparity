/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.javax.swing.component;

import javax.swing.JTextField;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserTextFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final BrowserTextFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new BrowserTextFactory();
		singletonLock = new Object();
	}

	public static JTextField create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private BrowserTextFactory() { super(); }

	private JTextField doCreate() {
		final JTextField jTextField = new JTextField();
		return jTextField;		
	}
}
