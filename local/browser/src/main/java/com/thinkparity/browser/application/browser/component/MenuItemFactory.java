/*
 * Jan 31, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
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

		final UIDefaults defaults = UIManager.getDefaults();
		// COLOR 215,231,244,255
		defaults.put("MenuItem.selectionBackground", new Color(215, 231, 244, 255));
		// COLOR BLACK
		defaults.put("MenuItem.selectionForeground", Color.BLACK);

        // COLOR 215,231,244,255
        defaults.put("CheckBoxMenuItem.selectionBackground", new Color(215, 231, 244, 255));
        // COLOR BLACK
        defaults.put("CheckBoxMenuItem.selectionForeground", Color.BLACK);
	}

	public static JMenuItem create(final String text, final Integer mnemonic) {
		synchronized(singletonLock) {
			return singleton.doCreate(text, mnemonic);
		}
	}

    public static JMenuItem create(final String text, final Integer mnemonic,
            final ActionListener l) {
        synchronized(singletonLock) {
            return singleton.doCreate(text, mnemonic, l);
        }
    }

    public static JCheckBoxMenuItem createCheckBox(final String text,
            final Integer mnemonic) {
        synchronized(singletonLock) {
            return singleton.doCreateCheckBox(text, mnemonic);
        }
    }

    public static JCheckBoxMenuItem createCheckBox(final String text,
            final Integer mnemonic, final ActionListener l) {
		synchronized(singletonLock) {
			return singleton.doCreateCheckBox(text, mnemonic, l);
		}
	}

	/**
	 * Create a MenuItemFactory [Singleton]
	 * 
	 */
	private MenuItemFactory() { super(); }

	private void applyMnemonic(final JMenuItem jMenuItem, final Integer mnemonic) {
		logger.debug("JMenuItem.Mnemonic[" + mnemonic + "]");
		jMenuItem.setMnemonic(mnemonic);
	}

    private JMenuItem doCreate(final String text) {
		logger.debug("JMenuItem[" + text + "]");
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
        logger.debug(text);
        final JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(text);
        applyDefaultFont(jCheckBoxMenuItem);
        applyMnemonic(jCheckBoxMenuItem, mnemonic);
        return jCheckBoxMenuItem;
    }

	private JCheckBoxMenuItem doCreateCheckBox(final String text,
            final Integer mnemonic, final ActionListener actionListener) {
        logger.debug(text);
        final JCheckBoxMenuItem jCheckBoxMenuItem = doCreateCheckBox(text, mnemonic);
        addActionListener(jCheckBoxMenuItem, actionListener);
        return jCheckBoxMenuItem;
    }
}
