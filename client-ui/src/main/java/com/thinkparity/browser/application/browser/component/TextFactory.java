/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.application.browser.component;

import javax.swing.JPasswordField;
import javax.swing.JTextField;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TextFactory extends ComponentFactory {

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

	public static JPasswordField createPassword() {
		synchronized(singletonLock) { return singleton.doCreatePassword(); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private TextFactory() { super(); }

	private JTextField doCreate() {
		final JTextField jTextField = new JTextField();
		applyDefaultFont(jTextField);
		return jTextField;		
	}
	private JPasswordField doCreatePassword() {
		final JPasswordField jPasswordField = new JPasswordField();
		applyDefaultFont(jPasswordField);
		return jPasswordField;
	}
}
