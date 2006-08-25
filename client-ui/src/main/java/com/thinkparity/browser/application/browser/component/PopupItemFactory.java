/*
 * Created On: Aug 11, 2006 1:46:13 PM
 */
package com.thinkparity.browser.application.browser.component;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.platform.AbstractFactory;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionFactory;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.ActionRegistry;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.ApplicationId;
import com.thinkparity.browser.platform.application.ApplicationRegistry;

/**
 * <b>Title:</b>thinkParity Popup Item Factory<br>
 * <b>Description:</b>The popup item factory understands swing menu items and
 * thinkParity actions. It uses the actions as a basis for creating the menu
 * items. The action name becomes the menu item text.<br>
 * It also creates a swing action wrapper around the thinkParity action in order
 * translate the action listener event into invoke. The wrappers are cached
 * inthernally as they are created and the data is replaced with each call to
 * create.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PopupItemFactory extends AbstractFactory {

    /** A singleton instance. */
    private static PopupItemFactory SINGLETON;
    
    static {
        final UIDefaults defaults = UIManager.getDefaults();
        defaults.put("MenuItem.selectionBackground", BrowserConstants.SelectionBackground);
        defaults.put("MenuItem.selectionForeground", BrowserConstants.SelectionForeground);
        defaults.put("MenuItem.font", BrowserConstants.Fonts.DefaultFont);
    }

    /**
     * Obtain an instance of a browser popup item factory
     * 
     * @return A popup item factory.
     */
    public static PopupItemFactory getInstance() {
        if (null == SINGLETON) {
            SINGLETON = new PopupItemFactory();
        }
        return SINGLETON;
    }

    /** An action registry. */
    private final ActionRegistry actionRegistry;

    /** The action wrapper registry. */
    private final Map<ActionId, ActionWrapper> wrapperRegistry;

    /** Create PopupItemFactory. */
    private PopupItemFactory() {
        super();
        this.actionRegistry = new ActionRegistry();
        this.wrapperRegistry = new HashMap<ActionId, ActionWrapper>(ActionId.values().length, 1.0F);
    }

    /**
     * Create a popup menu item for an action and its data.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @return A popup menu item.
     */
    public JMenuItem createPopupItem(final ActionId actionId,
            final Data data) {
        return SINGLETON.doCreatePopupItem(actionId, data);
    }

    /**
     * Create a popup menu item from an action id.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @return A popup menu item.
     */
    private JMenuItem doCreatePopupItem(final ActionId actionId, final Data data) {
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
     * A wrapper class for executing thinkParity actions from a popup menu.
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
            if (action.isSetAccelerator()) {
                final String s = action.getAccelerator();
                final KeyStroke k = KeyStroke.getKeyStroke(s);
                final KeyStroke k2 = KeyStroke.getKeyStroke("F1");
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke((String)action.getAccelerator()));
                //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
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
