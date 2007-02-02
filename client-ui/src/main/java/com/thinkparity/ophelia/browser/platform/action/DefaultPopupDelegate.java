/*
 * Created On:  4-Dec-06 12:08:27 PM
 */
package com.thinkparity.ophelia.browser.platform.action;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DefaultPopupDelegate implements PopupDelegate {

    /** An apache logger wrapper. */
    protected final Log4JWrapper logger;

    /** The popup invoker <code>Component</code>. */
    private Component invoker;

    /** A <code>PopupItemFactory</code>. */
    private final PopupItemFactory itemFactory;

    /** A <code>MenuFactory</code>. */
    private JPopupMenu jPopupMenu;

    /** A map of menu name <code>String</code> to existing <code>JMenu</code>s. */
    private final Map<String, JMenu> submenus;

    /** The popup location coordinate <code>int</code>s. */
    private int x, y;

    /**
     * Create DefaultPopupDelegate.
     *
     */
    public DefaultPopupDelegate() {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.submenus = new HashMap<String, JMenu>();
        this.itemFactory = PopupItemFactory.getInstance();
    }

    /**
     * Add an action to a submenu. The submenu's action id will determine the
     * name of the menu.
     * 
     * @param submenuActionId
     *            An <code>ActionId</code> for naming the submenu.
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void add(final ActionId submenuActionId, final ActionId actionId, final Data data) {
        final JMenu submenu = getSubmenu(submenuActionId);
        submenu.add(itemFactory.createPopupItem(actionId, data));      
    }

    /**
     * Add an action to the popup menu.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void add(final ActionId actionId, final Data data) {
        jPopupMenu.add(itemFactory.createPopupItem(actionId, data));
    }
    
    /**
     * Add a list of actions to the popup menu.
     * This allows more than one action to be associated with one menu item.
     * 
     * @param actionIds
     *            A list of <code>ActionId</code>.
     * @param dataList
     *            The list of action <code>Data</code>.
     * @param mainActionIndex
     *            The index to the main action (used for the name).            
     */
    public void add(final List<ActionId> actionIds,
            final List<Data> dataList, final int mainActionIndex) {
        jPopupMenu.add(itemFactory.createPopupItem(actionIds, dataList, mainActionIndex));
    }

    /**
     * Add an action to a submenu such that the action name is used for the parent
     * and the text is used for the child menu item. Example:
     *    Open - Doc1
     *           Doc2
     *           
     * @param actionId
     *            An <code>ActionId</code>.
     * @param text
     *            The menu text <code>String</code>.
     * @param data
     *            The action <code>Data</code>.                    
     */
    public void add(final ActionId actionId, final String text, final Data data) {
        final JMenu jMenu = getSubmenu(actionId);
        jMenu.add(itemFactory.createPopupItem(actionId, text, data));      
    }

    /**
     * Add an action to a menu.
     * 
     * @param jMenu
     *            A <code>JMenu</code>.
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void add(final JMenu jMenu, final ActionId actionId, final Data data) {
        jMenu.add(itemFactory.createPopupItem(actionId, data));
    }

    /**
     * Add a menu item to the popup menu.
     * 
     * @param menuItem
     *            A <code>JMenuItem</code>.
     */
    public void add(final JMenuItem menuItem) {
        jPopupMenu.add(menuItem);
    }

    /**
     * Add a plugin action.
     * 
     * @param pluginId
     *            A <code>PluginId</code>.
     * @param actionName
     *            An action name <code>String</code>.
     * @param selection
     *            An <code>Object</code> selection.
     */
    public void add(final PluginId pluginId, final String actionName,
            final Object selection) {
        itemFactory.addPopupItem(jPopupMenu, pluginId, actionName, selection);
    }
    
    /**
     * Add a string to the popup menu.
     * 
     * @param s
     *            A <code>String</code>.
     */
    public void add(final String s) {
        jPopupMenu.add(s);
    }
    
    /**
     * Add an action to a submenu. If the menu for the given text does not exist it
     * will be created. Example:
     *    Doc1 - Open
     *           Print
     * 
     * @param text
     *            The menu text <code>String</code>.
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void add(final String text, final ActionId actionId, final Data data) {
        final JMenu jMenu = getSubmenu(text);
        jMenu.add(itemFactory.createPopupItem(actionId, data));
    }

    /**
     * Add an action to a submenu. An action is supplied to determine the submenu.
     * Text is supplied for the name of the new menu item. Finally an action
     * is provided that will execute when the menu is selected by the user.
     * 
     * Example:
     * Action "edit profile" added to the submenu "open" as "open -> Joe Smith"
     * 
     * @param submenuActionId
     *            An <code>ActionId</code> for naming the submenu.
     * @param text
     *            The menu text <code>String</code>.
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void add(final ActionId subMenuActionId, final String text, final ActionId actionId, final Data data) {
        final JMenu jMenu = getSubmenu(subMenuActionId);
        jMenu.add(itemFactory.createPopupItem(actionId, text, data));
    }

    /**
     * Add a separator to the popup menu.
     * 
     */
    public void addSeparator() {
        jPopupMenu.addSeparator();
    }
    
    /**
     * Add a submenu separator.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     */
    public void addSeparator(final ActionId submenuActionId) {
        final JMenu submenu = getSubmenu(submenuActionId);
        submenu.addSeparator();  
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.PopupDelegate#initialize(java.awt.Component,
     *      int, int)
     * 
     */
    public void initialize(final Component invoker, final int x, final int y) {
        this.invoker = invoker;
        this.x = x;
        this.y = y;
        this.jPopupMenu = MenuFactory.createPopup();
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.PopupDelegate#initialize(java.awt.Component, int, int, int, int)
     *
     */
    public void initialize(final Component invoker, final int x, final int y,
            final int width, final int height) {
        initialize(invoker, x, y);
    }

    /**
     * Show the popup. Note that if the menu is null; or contains no elements;
     * nothing is done.
     * 
     */
    public void show() {
        logger.logApiId();
        logger.logVariable("invoker", invoker);
        logger.logVariable("x", x);
        logger.logVariable("y", y);
        jPopupMenu.show(invoker, x, y);
        invoker = jPopupMenu = null;
        submenus.clear();
        x = y = -1;
    }
    
    /**
     * Obtain a sub menu for an action. If the action already has a sub menu
     * retreive it; if not create a new one.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     * @return A <code>JMenu</code>.
     */
    private JMenu getSubmenu(final ActionId actionId) {
        final JMenu subMenu;
        final String menuName = itemFactory.getPopupActionName(actionId);
        if (submenus.containsKey(menuName)) {
            subMenu = submenus.get(menuName);
        } else {
            subMenu = MenuFactory.createPopupSubMenu(menuName);
            jPopupMenu.add(subMenu);
            submenus.put(menuName, subMenu);
        }
        return subMenu;
    }

    /**
     * Obtain a sub menu given its name. If it exists then
     * retreive it; if not create a new one.
     * 
     * @param menuName
     *            A menu name <code>String</code>.
     * @return A <code>JMenu</code>.
     */
    private JMenu getSubmenu(final String menuName) {
        final JMenu subMenu;
        if (submenus.containsKey(menuName)) {
            subMenu = submenus.get(menuName);
        } else {
            subMenu = MenuFactory.createPopupSubMenu(menuName);
            jPopupMenu.add(subMenu);
            submenus.put(menuName, subMenu);
        }
        return subMenu;
    }
}
