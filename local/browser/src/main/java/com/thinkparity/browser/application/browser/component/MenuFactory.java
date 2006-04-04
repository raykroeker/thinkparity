/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

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

	static {
        singleton = new MenuFactory();

        final UIDefaults defaults = UIManager.getDefaults();
        // COLOR 215,231,244,255
        defaults.put("Menu.selectionBackground", new Color(215, 231, 244, 255));
        // COLOR BLACK
        defaults.put("Menu.selectionForeground", Color.BLACK);

        // COLOR 215,231,244,255
//        defaults.put("CheckBoxMenuItem.selectionBackground", new Color(215, 231, 244, 255));
        // COLOR BLACK
//        defaults.put("CheckBoxMenuItem.selectionForeground", Color.BLACK);
	}

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
