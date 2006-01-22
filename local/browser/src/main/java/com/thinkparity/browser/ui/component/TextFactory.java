/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.ui.component;

import javax.swing.JTextField;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TextFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final TextFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new TextFactory();
		singletonLock = new Object();
	}

	public static JTextField create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private TextFactory() { super(); }

	private JTextField doCreate() {
		final JTextField jTextField = new JTextField();
		return jTextField;		
	}
}
