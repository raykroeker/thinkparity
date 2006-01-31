/*
 * Jan 31, 2006
 */
package com.thinkparity.browser.ui.component;

import javax.swing.JMenuItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MenuItemFactory extends ComponentFactory {

	private static final MenuItemFactory singleton;

	private static final Object singletonLock;

	static {
		singleton = new MenuItemFactory();
		singletonLock = new Object();
	}

	public static JMenuItem create(final String text, final Integer mnemonic) {
		synchronized(singletonLock) { return singleton.doCreate(text, mnemonic); }
	}

	/**
	 * Create a MenuItemFactory [Singleton]
	 * 
	 */
	private MenuItemFactory() { super(); }

	private JMenuItem doCreate(final String text) {
		final JMenuItem jMenuItem = new JMenuItem(text);
		applyDefaultFont(jMenuItem);
		return jMenuItem;
	}

	private JMenuItem doCreate(final String text, final Integer mnemonic) {
		final JMenuItem jMenuItem = doCreate(text);
		applyMnemonic(jMenuItem, mnemonic);
		return jMenuItem;
	}

	private void applyMnemonic(final JMenuItem jMenuItem, final Integer mnemonic) {
		jMenuItem.setMnemonic(mnemonic);
	}
}
