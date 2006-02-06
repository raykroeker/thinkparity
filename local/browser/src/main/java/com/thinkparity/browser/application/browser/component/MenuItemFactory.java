/*
 * Jan 31, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.Color;

import javax.swing.JMenuItem;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

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

	static {
		final UIDefaults defaults = UIManager.getDefaults();
		// COLOR 215, 231, 244, 255
		defaults.put("MenuItem.selectionBackground", new Color(215, 231, 244, 255));
		// COLOR BLACK
		defaults.put("MenuItem.selectionForeground", Color.BLACK);
	}

	private JMenuItem doCreate(final String text) {
		final JMenuItem jMenuItem = new JMenuItem(text);
		applyDefaultFont(jMenuItem);
		applyHandCursor(jMenuItem);
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
