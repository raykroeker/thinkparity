/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.BrowserMenu;

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
        defaults.put("Menu.selectionBackground", BrowserConstants.SelectionBackground);
        defaults.put("Menu.selectionForeground", BrowserConstants.SelectionForeground);
        defaults.put("Menu.font", BrowserConstants.Fonts.DefaultFont);
	}

    /**
     * Create a JMenu.
     * 
     * @param text
     *          The menu text.
     * @return The JMenu.
     */    
    public static JMenu create(final String text) {
        return singleton.doCreate(text);
    }

    /**
     * Create a JMenu.
     * 
     * @param text
     *          The menu text.
     * @param mnemonic
     *          The mnemonic.
     * @return The JMenu.
     */
    public static JMenu create(final String text, final Integer mnemonic) {
        return singleton.doCreate(text, mnemonic);
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

    private JMenu doCreate(final String text) {
        final BrowserMenu browserMenu = new BrowserMenu(text);
        // Note that background is transparent so it won't draw.
        // Then, browserMenu overrides paintComponent to paint a gradient.
        browserMenu.setBackground(new Color(255,255,255,0));
        return browserMenu;
        }
    
    private JMenu doCreate(final String text, final Integer mnemonic) {
        final JMenu jMenu = doCreate(text);
        jMenu.setMnemonic(mnemonic);
        return jMenu;
    }
}
