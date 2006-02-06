/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.application.browser.component;

import javax.swing.JList;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ListFactory extends ComponentFactory {

	private static final ListFactory singleton;

	private static final Object singletonLock;

	static {
		singleton = new ListFactory();
		singletonLock = new Object();
	}

	public static JList create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	/**
	 * Create a ListFactory.
	 */
	private ListFactory() { super(); }

	private JList doCreate() { return new JList(); }
}
