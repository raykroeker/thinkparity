/*
 * Jan 31, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.thinkparity.browser.application.browser.BrowserConstants;

/**
 * A swing menu item factory. Use to create menu items; and check box menu
 * items.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MenuItemFactory extends ComponentFactory {

    /**
     * The singelton factory.
     * 
     */
	private static final MenuItemFactory SINGLETON;

	static {
		SINGLETON = new MenuItemFactory();

		final UIDefaults defaults = UIManager.getDefaults();
		defaults.put("MenuItem.selectionBackground", BrowserConstants.SelectionBackground);
		defaults.put("MenuItem.selectionForeground", BrowserConstants.SelectionForeground);
        defaults.put("CheckBoxMenuItem.selectionBackground", BrowserConstants.SelectionBackground);
        defaults.put("CheckBoxMenuItem.selectionForeground", BrowserConstants.SelectionForeground);
	}

    /**
     * Create a menu item.
     * 
     * @param text
     *            The menu item text.
     * @param mnemonic
     *            The menu item mnemonic.
     * @return The menu item.
     */
	public static JMenuItem create(final String text, final Integer mnemonic) {
		return SINGLETON.doCreate(text, mnemonic);
	}

    /**
     * Create a menu item.
     * 
     * @param text
     *            The menu item text.
     * @param mnemonic
     *            The menu item mnemonic.
     * @param l
     *            The menu item action listener.
     * @return The menu item.
     */
    public static JMenuItem create(final String text, final Integer mnemonic,
            final ActionListener l) {
        return SINGLETON.doCreate(text, mnemonic, l);
    }

    /**
     * Create a check box menu item.
     * 
     * @param text
     *            The menu item text.
     * @param mnemonic
     *            The menu item mnemonic.
     * @return The menu item.
     */
    public static JCheckBoxMenuItem createCheckBox(final String text,
            final Integer mnemonic) {
        return SINGLETON.doCreateCheckBox(text, mnemonic);
    }

    /**
     * Create a menu item.
     * 
     * @param text
     *            The menu item text.
     * @param mnemonic
     *            The menu item mnemonic.
     * @param l
     *            The check box menu item action listener.
     * @return The menu item.
     */
    public static JCheckBoxMenuItem createCheckBox(final String text,
            final Integer mnemonic, final ActionListener l) {
		return SINGLETON.doCreateCheckBox(text, mnemonic, l);
	}

	/**
	 * Create a MenuItemFactory [Singleton, Factory]
	 * 
	 */
	private MenuItemFactory() { super(); }

    /**
     * Apply an integer mnemonic to the menu item.
     * 
     * @param jMenuItem
     *            The menu item.
     * @param mnemonic
     *            The integer mnemonic.
     */
	private void applyMnemonic(final JMenuItem jMenuItem, final Integer mnemonic) {
		jMenuItem.setMnemonic(mnemonic);
	}

    private JMenuItem doCreate(final String text) {
		final JMenuItem jMenuItem = new JMenuItem(text);
		applyDefaultFont(jMenuItem);
		applyMinimumWidth(jMenuItem, 150);
		return jMenuItem;
	}

    private JMenuItem doCreate(final String text, final Integer mnemonic) {
		final JMenuItem jMenuItem = doCreate(text);
		applyMnemonic(jMenuItem, mnemonic);
		return jMenuItem;
	}

    private JMenuItem doCreate(final String text, final Integer mnemonic,
            final ActionListener l) {
        final JMenuItem jMenuItem = doCreate(text, mnemonic);
        addActionListener(jMenuItem, l);
        return jMenuItem;
    }
	private JCheckBoxMenuItem doCreateCheckBox(final String text,
            final Integer mnemonic) {
        final JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(text);
        applyDefaultFont(jCheckBoxMenuItem);
        applyMnemonic(jCheckBoxMenuItem, mnemonic);
        return jCheckBoxMenuItem;
    }

	private JCheckBoxMenuItem doCreateCheckBox(final String text,
            final Integer mnemonic, final ActionListener actionListener) {
        final JCheckBoxMenuItem jCheckBoxMenuItem = doCreateCheckBox(text, mnemonic);
        addActionListener(jCheckBoxMenuItem, actionListener);
        return jCheckBoxMenuItem;
    }
}
