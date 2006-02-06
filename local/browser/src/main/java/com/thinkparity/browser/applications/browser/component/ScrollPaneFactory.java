/*
 * Feb 2, 2006
 */
package com.thinkparity.browser.applications.browser.component;

import javax.swing.JScrollPane;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ScrollPaneFactory extends ComponentFactory {

	private static final ScrollPaneFactory singleton;

	private static final Object singletonLock;

	static {
		singleton = new ScrollPaneFactory();
		singletonLock = new Object();
	}

	public static JScrollPane create() {
		synchronized(singletonLock) { return singleton.doCreate(); }
	}

	/**
	 * Create a ListFactory.
	 */
	private ScrollPaneFactory() { super(); }

	private JScrollPane doCreate() { return new JScrollPane(); }
}
