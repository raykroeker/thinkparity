/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.component;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MenuFactory {

	/**
	 * Singleton factory.
	 * 
	 */
	private static final MenuFactory singleton;

	static { singleton = new MenuFactory(); }

    public static JMenu create(final String text) {
        return singleton.doCreate(text);
    }

    /**
	 * Create a JPopupMenu.
	 * 
	 * @return The JPopupMenu.
	 */
	public static JPopupMenu createPopup() {
		return singleton.doCreatePopup();
	}

	/**
	 * Create a MenuFactory [Singleton, Factory]
	 * 
	 */
	private MenuFactory() { super(); }

	/**
	 * Create a JPopupMenu.
	 * 
	 * @return The JPopupMenu.
	 */
	private JPopupMenu doCreatePopup() {
		return new JPopupMenu();
	}

    private JMenu doCreate(final String text) { return new JMenu(text); }
}
