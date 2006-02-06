/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.applications.browser.component;

import javax.swing.JComboBox;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ComboBoxFactory extends ComponentFactory {

	private static final ComboBoxFactory singleton;

	private static final Object singletonLock;

	static {
		singleton = new ComboBoxFactory();
		singletonLock = new Object();
	}

	public static JComboBox create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	/**
	 * Create a ListFactory.
	 */
	private ComboBoxFactory() { super(); }

	private JComboBox doCreate() { return new JComboBox(); }
}
