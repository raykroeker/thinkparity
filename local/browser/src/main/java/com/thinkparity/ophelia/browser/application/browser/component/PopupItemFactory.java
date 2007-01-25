/*
 * Created On: Aug 11, 2006 1:46:13 PM
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import com.thinkparity.codebase.assertion.Assert;

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
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityMenuItem;

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
    
    /** Create PopupItemFactory. */
    private PopupItemFactory() {
        super();
        this.actionRegistry = new ActionRegistry();
        this.extensionRegistry = new HashMap<ActionExtension, ActionWrapper>();
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
     * Create a popup menu item that will run multiple actions.
     * This method is suited for context menus (mnemonic and
     * accelerator not supported, and popup name used).
     * 
     * @param actionIds
     *            A list of action id.
     * @param dataList
     *            A list of action data.
     * @param mainActionIndex
     *            The index to the main action (used for the name).       
     * @return A popup menu item.
     */
    public JMenuItem createPopupItem(final List<ActionId> actionIds,
            final List<Data> dataList, final int mainActionIndex) {
        return SINGLETON.doCreatePopupItem(actionIds, dataList, mainActionIndex, Boolean.FALSE);
    }
    
    /**
     * Create a popup menu item for an action and its data.
     * This method is suited for context menus.
     * This method supplies the menu name, so the AbstractAction
     * can't be used.
     * 
     * @param actionId
     *            An action id.
     * @param data
     *            The action data.
     * @param name
     *            The menu item name.         
     * @return A popup menu item.
     */
    public JMenuItem createPopupItem(final ActionId actionId,
            final String text, final Data data) {
        return SINGLETON.doCreatePopupItem(actionId, text, data);
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
                
                final Data data = new Data(1);
                data.set(ActionExtensionAction.DataKey.SELECTION, selection);                
                final ActionWrapper wrapper = getWrapper(extension, action, data);        
                jPopupMenu.add(new ThinkParityMenuItem(wrapper));
            }
        }
    }
    
    /**
     * Get action name.
     * 
     * @param actionId
     *            An action id.
     * @return The action name.     
     */
    public String getPopupActionName(final ActionId actionId) {
        return SINGLETON.doGetPopupActionName(actionId);
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
    private JMenuItem doCreatePopupItem(final ActionId actionId, final Data data, final Boolean mainMenu) {
        final AbstractAction action;
        if (actionRegistry.contains(actionId)) {
            action = actionRegistry.get(actionId);
        } else {
            action = ActionFactory.create(actionId);
        }
        
        final ActionWrapper actionWrapper = getWrapper(action, data, mainMenu);        
        
        return new ThinkParityMenuItem(actionWrapper);
    }
    
    /**
     * Create a popup menu item that will run multiple actions.
     * 
     * @param actionIds
     *            A list of action id.
     * @param dataList
     *            A list of action data.
     * @param mainActionIndex
     *            The index to the main action (used for the name).
     * @param mainMenu
     *            true for main menu, false for context menu         
     * @return A popup menu item.
     */
    private JMenuItem doCreatePopupItem(final List<ActionId> actionIds,
            final List<Data> dataList, final int mainActionIndex, final Boolean mainMenu) {
        Assert.assertTrue(actionIds.size()==dataList.size(), "List size mismatch creating popup menu.");  
        AbstractAction action;
        ActionId actionId = actionIds.get(0);
        if (actionRegistry.contains(actionId)) {
            action = actionRegistry.get(actionId);
        } else {
            action = ActionFactory.create(actionId);
        }
        final ActionWrapper actionWrapper = getWrapper(action, dataList.get(0), mainMenu);

        for (int index = 1; index < actionIds.size(); index++) {
            actionId = actionIds.get(index);
            if (actionRegistry.contains(actionId)) {
                action = actionRegistry.get(actionId);
            } else {
                action = ActionFactory.create(actionId);
            }
            actionWrapper.addAction(action, dataList.get(index), index==mainActionIndex);
        }
        
        return new ThinkParityMenuItem(actionWrapper);
    }
    
    /**
     * Create a popup menu item from an action id.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     * @param text
     *            The menu item text <code>String</code>.
     * @param data
     *            The action <code>Data</code>.
     * @return A <code>JMenuItem</code>.
     */
    private JMenuItem doCreatePopupItem(final ActionId actionId,
            final String text, final Data data) {
        final AbstractAction action;
        if (actionRegistry.contains(actionId)) {
            action = actionRegistry.get(actionId);
        } else {
            action = ActionFactory.create(actionId);
        }
        
        final ActionWrapper actionWrapper = getWrapper(action, data, Boolean.FALSE);        
        
        final JMenuItem menuItem = new ThinkParityMenuItem(text);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                actionWrapper.actionPerformed(e);
            }           
        });
        return menuItem;
    }
    
    /**
     * Get action name.
     * 
     * @param actionId
     *            An action id.
     * @return The action name.     
     */    
    private String doGetPopupActionName(final ActionId actionId) {   
        final AbstractAction action;
        if (actionRegistry.contains(actionId)) {
            action = actionRegistry.get(actionId);
        } else {
            action = ActionFactory.create(actionId);
        }
        return action.getName();
    }

    /**
     * Obtain an action wrapper for an action extension. The wrapper will be
     * created and registered if required.
     * 
     * @param extension
     *            A <code>ActionExtension</code>.
     * @param action
     *            An <code>AbstractAction</code>.
     * @param data
     *            The <code>Data</code>.       
     * @return An <code>ActionWrapper</code>.
     */
    private ActionWrapper getWrapper(final ActionExtension extension,
            final AbstractAction action, final Data data) {
        if (extensionRegistry.containsKey(extension)) {
            final ActionWrapper actionWrapper = extensionRegistry.get(extension);
            actionWrapper.setData(data);
            return actionWrapper;
        } else {
            extensionRegistry.put(extension, new ActionWrapper(action, data, Boolean.FALSE));
            return extensionRegistry.get(extension);
        }
    }

    /**
     * Create an action wrapper using the appropriate map.
     * 
     * @param action
     *              The AbstractAction.
     * @param data
     *              The Data.
     * @param mainMenu
     *            true for main menu, false for context menu             
     * @return An ActionWrapper.
     */
    private ActionWrapper getWrapper(final AbstractAction action,
            final Data data, final Boolean mainMenu) {
        return new ActionWrapper(action, data, mainMenu);
    }

    /**
     * A wrapper class for executing thinkParity actions from a popup menu.
     * 
     */
    private class ActionWrapper extends javax.swing.AbstractAction {

        /** A list of thinkParity actions. */
        private final List<AbstractAction> actions;

        /** A list of action data. */
        private List<Data> dataList;
        
        /** Flag indicating if this is for the main menu. */
        private Boolean mainMenu;

        /**
         * Create an ActionWrapper for one action and its data.
         * 
         * @param action
         *            A thinkParity action.
         * @param data
         *            The action data.
         * @param mainMenu
         *            true for main menu, false for context menu       
         */
        private ActionWrapper(final AbstractAction action,
                final Data data, final Boolean mainMenu) {
            super();
            this.mainMenu = mainMenu;
            this.actions = new ArrayList<AbstractAction>();
            this.dataList = new ArrayList<Data>();
            actions.add(action);
            dataList.add(data);
            adjustName(mainMenu, action);       
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         * 
         */
        public void actionPerformed(final ActionEvent e) {
            try {
                // Run all actions in sequence. Most often there will be just one action.
                for (int index = 0; index < actions.size(); index++) {
                    final AbstractAction action = actions.get(index);
                    final Data data = dataList.get(index);
                    action.invokeAction(data);
                }
            } catch(final Throwable t) {
                ((Browser) new ApplicationRegistry().get(ApplicationId.BROWSER))
                        .displayErrorDialog(t);
            }
        }

        /**
         * Add an action and its data. This makes it possible to invoke more
         * than one thinkParity action from a single popup menu item.
         * 
         * @param action
         *            A thinkParity action.
         * @param data
         *            The action data.
         * @param useName
         *            True if this action is used to set the menu name.        
         */
        private void addAction(final AbstractAction action,
                final Data data, final Boolean useName) {
            this.actions.add(action);
            this.dataList.add(data);
            if (useName) {
                adjustName(mainMenu, action);
            }
        }

        /**
         * Set the name, tailor the action so it is suited for main menu or popup menu.
         */
        private void adjustName(final Boolean mainMenu, final AbstractAction action) {
            if (mainMenu) {
                putValue(Action.NAME, action.isSetMenuName() ? action.getMenuName() : "!No name set.!"); 
                setMnemonicAndAccelerator(action);
            } else {
                putValue(Action.NAME, action.isSetName() ? action.getName() : "!No name set.!");
            }
        }

        /**
         * Set the action data.
         * 
         * @param data
         *            THe action data.
         */
        private void setData(final Data data) {
            this.dataList.set(0, data);
        }

        /**
         * Set mnemonic and accelerator
         */
        private void setMnemonicAndAccelerator(final AbstractAction action) {
            if (action.isSetMnemonic()) {
                putValue(MNEMONIC_KEY, Integer.valueOf(action.getMnemonic().charAt(0)));
            }
            if (action.isSetAccelerator()) {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(action.getAccelerator())); 
            }
        }
    }
}
