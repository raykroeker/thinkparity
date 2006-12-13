/*
 * Created On:  4-Dec-06 12:08:27 PM
 */
package com.thinkparity.ophelia.browser.platform.action;

import java.awt.Component;
import java.util.HashMap;
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

    /** A map of menu name <code>String</code> to existing <code>JMenu</code>s. */
    private final Map<String, JMenu> jMenus;

    /** A <code>MenuFactory</code>. */
    private JPopupMenu jPopupMenu;

    /** The popup location coordinate <code>int</code>s. */
    private int x, y;

    /**
     * Create DefaultPopupDelegate.
     *
     */
    public DefaultPopupDelegate() {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.jMenus = new HashMap<String, JMenu>();
        this.itemFactory = PopupItemFactory.getInstance();
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
     * Add an action to a menu. If the menu for the given text does not exist it
     * will be created.
     * 
     * @param text
     *            The menu texxt <code>String</code>.
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The action <code>Data</code>.
     */
    public void add(final String text, final ActionId actionId, final Data data) {
        final JMenu jMenu;
        if (jMenus.containsKey(text)) {
            jMenu = jMenus.get(text);
        } else {
            jMenu = MenuFactory.createPopupSubMenu(text);
            jPopupMenu.add(jMenu);
            jMenus.put(text,  jMenu);
        }
        jMenu.add(itemFactory.createPopupItem(actionId, data));
    }

    /**
     * Add a separator to the popup menu.
     * 
     */
    public void addSeparator() {
        jPopupMenu.addSeparator();
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
        jMenus.clear();
        x = y = -1;
    }
}
