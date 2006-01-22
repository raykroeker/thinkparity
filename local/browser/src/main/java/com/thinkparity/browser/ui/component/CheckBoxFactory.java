/*
 * Jan 13, 2006
 */
package com.thinkparity.browser.ui.component;

import javax.swing.JCheckBox;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CheckBoxFactory {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final CheckBoxFactory singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new CheckBoxFactory();
		singletonLock = new Object();
	}

	public static JCheckBox create(final String text) {
		synchronized(singletonLock) { return singleton.doCreate(text); }
	}

	/**
	 * Create a ButtonFactory.
	 */
	private CheckBoxFactory() { super(); }

	private JCheckBox doCreate(final String text) {
		final JCheckBox jCheckBox = new JCheckBox(text);
		return jCheckBox;		
	}
}
