/*
 * Mar 10, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.Color;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.BrowserMenu;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MenuFactory {

	/** A singleton instance. */
	private static final MenuFactory SINGLETON;

	static { SINGLETON = new MenuFactory(); }

    /**
     * Create a JMenu.
     * 
     * @param text
     *          The menu text.
     * @return The JMenu.
     */    
    public static JMenu create(final String text) {
        return SINGLETON.doCreate(text);
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
        return SINGLETON.doCreate(text, mnemonic);
    }

    /**
	 * Create a JPopupMenu.
	 * 
	 * @return The JPopupMenu.
	 */
	public static JPopupMenu createPopup() {
		return SINGLETON.doCreatePopup();
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
