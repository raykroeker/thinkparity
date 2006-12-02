/*
 * Created On: Aug 11, 2006 1:46:13 PM
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.AbstractFactory;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.Plugin;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtensionAction;

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

    /** The action extension action wrapper map. */
    private final Map<ActionExtension, ActionWrapper> extensionRegistry;
    
    /** The action wrapper registry (for main menus). */
    private final Map<ActionId, ActionWrapper> menuWrapperRegistry;

    /** The action wrapper registry (for context popup menus). */
    private final Map<ActionId, ActionWrapper> wrapperRegistry;
    
    /** Create PopupItemFactory. */
    private PopupItemFactory() {
        super();
        this.actionRegistry = new ActionRegistry();
        this.extensionRegistry = new HashMap<ActionExtension, ActionWrapper>();
        this.menuWrapperRegistry = new HashMap<ActionId, ActionWrapper>(ActionId.values().length, 1.0F);
        this.wrapperRegistry = new HashMap<ActionId, ActionWrapper>(ActionId.values().length, 1.0F);
    }

    /**
     * Add a popup item for an action extension (plugin) to the menu.
     * @param extension
     * @param selection
     * @return
     */
    public void addPopupItem(final JPopupMenu jPopupMenu,
            final PluginId pluginId, final String name, final Object selection) {
        SINGLETON.doAddPopupItem(jPopupMenu, pluginId, name, selection);
    }

    /**
     * Create a popup menu item for an action and its data.
     * This method is suited for main menus (mnemonic and
     * accelerator supported, and menu name used).
     */
    public JMenuItem createMenuPopupItem(final ActionId actionId,
            final Data data) {
        return SINGLETON.doCreatePopupItem(actionId, data, Boolean.TRUE);        
    }

    /**
     * Create a popup menu item for an action and its data.
     * This method is suited for context menus (mnemonic and
     * accelerator not supported, and popup name used).
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @return A popup menu item.
     */
    public JMenuItem createPopupItem(final ActionId actionId,
            final Data data) {
        return SINGLETON.doCreatePopupItem(actionId, data, Boolean.FALSE);
    }

    /**
     * Create a popup menu item from an action id.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @param mainMenu
     *            true for main menu, false for context menu
     * @return A popup menu item.
     */
    public void doAddPopupItem(final JPopupMenu jPopupMenu,
            final PluginId pluginId, final String name, final Object selection) {
        final PluginRegistry pluginRegistry = new PluginRegistry();
        final Plugin plugin = pluginRegistry.getPlugin(pluginId);
        if (null != plugin) {
            final ActionExtension extension =
                pluginRegistry.getActionExtension(pluginId, name);
            if (null != extension) {
                final AbstractAction action;
                if (actionRegistry.contains(extension)) {
                    action = actionRegistry.get(extension);
                } else {
                    action = ActionFactory.create(extension);
                }

                final ActionWrapper wrapper = getWrapper(extension, action);        
                final Data data = new Data(1);
                data.set(ActionExtensionAction.DataKey.SELECTION, selection);
                wrapper.setData(data);
                jPopupMenu.add(new JMenuItem(wrapper));
            }
        }
    }

    /**
     * Create a popup menu item from an action id.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @param mainMenu
     *            true for main menu, false for context menu
     * @return A popup menu item.
     */
    private JMenuItem doCreatePopupItem(final ActionId actionId, final Data data, Boolean mainMenu) {
        final AbstractAction action;
        if (actionRegistry.contains(actionId)) {
            action = actionRegistry.get(actionId);
        } else {
            action = ActionFactory.create(actionId);
        }
        
        final Map<ActionId, ActionWrapper> registry;
        if (mainMenu) {
            registry = menuWrapperRegistry;
        }
        else {
            registry = wrapperRegistry;            
        }
        
        final ActionWrapper actionWrapper = getWrapper(actionId, action, registry);        
        actionWrapper.setData(data);
        
        // Adjust the action so it is suited to main menu or context popup menu
        actionWrapper.adjustForMenuType(mainMenu);
        
        return new JMenuItem(actionWrapper);
    }

    /**
     * Obtain an action wrapper for an action extension. The wrapper will be
     * created and registered if required.
     * 
     * @param extension
     *            A <code>ActionExtension</code>.
     * @param action
     *            An <code>AbstractAction</code>.
     * @return An <code>ActionWrapper</code>.
     */
    private ActionWrapper getWrapper(final ActionExtension extension,
            final AbstractAction action) {
        if (extensionRegistry.containsKey(extension)) {
            return extensionRegistry.get(extension);
        } else {
            extensionRegistry.put(extension, new ActionWrapper(action));
            return extensionRegistry.get(extension);
        }
    }

    /**
     * Create an action wrapper using the appropriate map.
     * 
     * @param actionId
     *              The action ID.
     * @param action
     *              The action.
     * @param registry
     *              The registry, either for menus or for context popups.
     * @return An ActionWrapper.
     */
    private ActionWrapper getWrapper(final ActionId actionId,
            final AbstractAction action,
            final Map<ActionId, ActionWrapper> registry) {
        final ActionWrapper actionWrapper;
        if (registry.containsKey(actionId)) {
            actionWrapper = registry.get(actionId);
        }
        else {
            registry.put(actionId, new ActionWrapper(action));
            actionWrapper = registry.get(actionId);
        }
        
        return actionWrapper;
    }

    /**
     * A wrapper class for executing thinkParity actions from a popup menu.
     * 
     */
    private class ActionWrapper extends javax.swing.AbstractAction {

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
            if (action.isSetName()) {
                putValue(Action.NAME, action.getName());
            } else if  (action.isSetMenuName()) {
                putValue(Action.NAME, action.getMenuName());
            } else {
                putValue(Action.NAME, "!No name set.!");
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
         * Tailor the action so it is suited for main menu or popup menu.
         */
        public void adjustForMenuType(Boolean mainMenu) {
            if (mainMenu) {
                putValue(Action.NAME, action.isSetMenuName() ? action.getMenuName() : "!No name set.!"); 
                setMnemonicAndAccelerator();
            }
            else {
                putValue(Action.NAME, action.isSetName() ? action.getName() : "!No name set.!");
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
        
        /**
         * Set mnemonic and accelerator
         */
        private void setMnemonicAndAccelerator() {
            if (action.isSetMnemonic()) {
                putValue(MNEMONIC_KEY, Integer.valueOf(action.getMnemonic().charAt(0)));
            }
            if (action.isSetAccelerator()) {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(action.getAccelerator())); 
            }
        }
    }
}
