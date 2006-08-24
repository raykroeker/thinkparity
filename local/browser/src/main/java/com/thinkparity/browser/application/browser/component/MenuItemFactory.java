/*
 * Jan 31, 2006
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.ActionRegistry;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;

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
    
    /** An action registry. */
    private final ActionRegistry actionRegistry;
    
    /** The action wrapper registry. */
    private final Map<ActionId, ActionWrapper> wrapperRegistry;

    /**
     * Create a menu item.
     * 
     * @param text
     *            The menu item text.
     * @param mnemonic
     *            The menu item mnemonic.
     * @return The menu item.
     */
/*	public static JMenuItem create(final String text, final Integer mnemonic) {
		return SINGLETON.doCreate(text, mnemonic);
	}*/

    /**
     * Create a menu item.
     *
     * @param text
     *      The menu item text.
     * @param mnemonic
     *      The menu item mnemonic.
     * @param l
     *      The action listener.
     * @return The menu item.
     */
/*    public static JMenuItem create(final String text, final Integer mnemonic, final ActionListener l) {
        return SINGLETON.doCreate(text, mnemonic, l);
    }*/
    
    /**
     * Create a menu item for an action and its data.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @return A menu item.
     */
    public static JMenuItem createMenuItem(final ActionId actionId, final Data data) {
        return SINGLETON.doCreateMenuItem(actionId, data);
    }
    
    /**
     * Create a menu item from an action id.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @return A menu item.
     */
    private JMenuItem doCreateMenuItem(final ActionId actionId, final Data data) {
        final AbstractAction action;
        if (actionRegistry.contains(actionId)) {
            action = actionRegistry.get(actionId);
        } else {
            action = ActionFactory.create(actionId);
        }
        final ActionWrapper actionWrapper;
        if (wrapperRegistry.containsKey(actionId)) {
            actionWrapper = wrapperRegistry.get(actionId);
        } else {
            wrapperRegistry.put(actionId, new ActionWrapper(action));
            actionWrapper = wrapperRegistry.get(actionId);
        }
        actionWrapper.setData(data);
        return new JMenuItem(actionWrapper);
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
/*    public static JCheckBoxMenuItem createCheckBox(final String text,
            final Integer mnemonic) {
        return SINGLETON.doCreateCheckBox(text, mnemonic);
    }*/

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
/*    public static JCheckBoxMenuItem createCheckBox(final String text,
            final Integer mnemonic, final ActionListener l) {
		return SINGLETON.doCreateCheckBox(text, mnemonic, l);
	}*/

	/**
	 * Create a MenuItemFactory [Singleton, Factory]
	 * 
	 */
	private MenuItemFactory() {
        super();
        this.actionRegistry = new ActionRegistry();
        this.wrapperRegistry = new HashMap<ActionId, ActionWrapper>(ActionId.values().length, 1.0F);
    }

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
    
    /**
     * A wrapper class for executing thinkParity actions from a menu.
     * 
     */
    private class ActionWrapper extends javax.swing.AbstractAction {

        /** @see java.io.Serializable */
        private static final long serialVersionUID = 1;

        /** A thinkParity action. */
        private final AbstractAction action;

        /** The action data. */
        private Data data;

        /**
         * Create SwingAbstractAction.
         * 
         * @param action
         *            A thinkParity action.
         * @param data
         *            The action data.
         */
        private ActionWrapper(final AbstractAction action) {
            super();
            this.action = action;
            this.data = null;
            putValue(Action.NAME, action.isSetName() ? action.getName() : "!No name set.!");
            if (action.isSetMnemonic()) {
                putValue(MNEMONIC_KEY, new Integer(action.getMnemonic().charAt(0)));
            }
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         * 
         */
        public void actionPerformed(final ActionEvent e) {
            try {
                action.invoke(data);
            } catch(final Throwable t) {
                ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER))
                        .displayErrorDialog(t.getLocalizedMessage(), t);
            }
        }

        /**
         * Set the action data.
         * 
         * @param data
         *            THe action data.
         */
        public void setData(final Data data) {
            this.data = data;
        }
    }
}
