/*
 * Created On: Aug 11, 2006 1:46:13 PM
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.platform.AbstractFactory;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionFactory;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionRegistry;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
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
public final class PopupItemFactory extends AbstractFactory {

    /**
     * A <code>Map</code> of <code>ApplicationId</code>s to their
     * <code>PopupItemFactory</code> instances.
     */
    private static final Map<ApplicationId, PopupItemFactory> INSTANCES;

    static {
        INSTANCES = new HashMap<ApplicationId, PopupItemFactory>(
                ApplicationId.values().length);
    }

    /**
     * Obtain an instance of a browser popup item factory
     * 
     * @return A popup item factory.
     */
    public static PopupItemFactory getInstance(final Application application) {
        synchronized (INSTANCES) {
            if (!INSTANCES.containsKey(application.getId())) {
                INSTANCES.put(application.getId(), new PopupItemFactory(application));
            }
            return INSTANCES.get(application.getId());
        }
    }

    /** An action registry. */
    private final ActionRegistry actionRegistry;

    /** An <code>Application</code>. */
    private final Application application;

    /**
     * Create PopupItemFactory.
     * 
     * @param application
     *            An <code>Application</code>.
     */
    private PopupItemFactory(final Application application) {
        super();
        this.application = application;
        this.actionRegistry = new ActionRegistry();
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
    public JMenuItem createPopupItem(final ActionId actionId, final Data data) {
        return doCreatePopupItem(actionId, data, Boolean.FALSE);
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
        return doCreatePopupItem(actionIds, dataList, mainActionIndex, Boolean.FALSE);
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
    private JMenuItem doCreatePopupItem(final ActionId actionId,
            final Data data, final Boolean mainMenu) {
        final AbstractAction action;
        if (actionRegistry.contains(actionId)) {
            action = actionRegistry.get(actionId);
        } else {
            action = ActionFactory.create(actionId);
        }
        return new ThinkParityMenuItem(getWrapper(action, data, mainMenu));
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
            actionWrapper.addAction(application, action, dataList.get(index),
                    index == mainActionIndex);
        }
        
        return new ThinkParityMenuItem(actionWrapper);
    }

    /**
     * Create an action wrapper using the appropriate map.
     * 
     * @param action
     *              An <code>AbstractAction</code>.
     * @param input
     *              The action's input <code>Data</code>.
     * @param mainMenu
     *            true for main menu, false for context menu             
     * @return An ActionWrapper.
     */
    private ActionWrapper getWrapper(final AbstractAction action,
            final Data input, final Boolean mainMenu) {
        return new ActionWrapper(application, action, input, mainMenu);
    }

    /**
     * A wrapper class for executing thinkParity actions from a popup menu.
     * 
     */
    private class ActionWrapper extends javax.swing.AbstractAction {

        /** A <code>Map</code> of the action to its corresponding <code>Application</code>. */
        private final Map<AbstractAction, Application> actionApplications;

        /**
         * A <code>Map</code> of the <code>AbstractAction</code> to its
         * input <code>Data</code>.
         */
        private final Map<AbstractAction, Data> actionInput;
        
        /** A <code>List</code> of <code>AbstractAction</code>s to invoke. */
        private final List<AbstractAction> actions;

        /** Flag indicating if this is for the main menu. */
        private final Boolean mainMenu;

        private ActionWrapper(final Application application,
                final AbstractAction action, final Data data,
                final Boolean mainMenu) {
            super();
            this.mainMenu = mainMenu;
            this.actions = new ArrayList<AbstractAction>(2);
            this.actionApplications = new HashMap<AbstractAction, Application>(2);
            this.actionInput = new HashMap<AbstractAction, Data>(2);

            actions.add(action);
            actionApplications.put(action, application);
            actionInput.put(action, data);

            adjustName(mainMenu, action);       
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         * 
         */
        public void actionPerformed(final ActionEvent e) {
            // run actions in sequence
            for (final AbstractAction action : actions) {
                action.invokeAction(actionApplications.get(action),
                        actionInput.get(action));
            }
        }

        /**
         * Add an action and its data. This makes it possible to invoke more
         * than one thinkParity action from a single popup menu item.
         * 
         * @param action
         *            A thinkParity action.
         * @param input
         *            The action's input <code>Data</code>.
         * @param useName
         *            True if this action is used to set the menu name.        
         */
        private void addAction(final Application application,
                final AbstractAction action, final Data input,
                final Boolean useName) {
            this.actions.add(action);
            this.actionApplications.put(action, application);
            this.actionInput.put(action, input);

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
