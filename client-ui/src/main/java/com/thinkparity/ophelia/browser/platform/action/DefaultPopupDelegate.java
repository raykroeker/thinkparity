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
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu;
import com.thinkparity.ophelia.browser.util.swing.plaf.ThinkParityPopupMenu.MenuBackgroundType;

/**
 * <b>Title:</b>thinkParity OpheliaUI Default Popup Delegate<br>
 * <b>Description:</b>A default implementation of a popup delegate used to
 * create/show a swing popup menus for thinkParity actions.<br>
 * 
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
     * @param application
     *            An <code>Application</code>.
     */
    public DefaultPopupDelegate(final Application application) {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.submenus = new HashMap<String, JMenu>();
        this.itemFactory = PopupItemFactory.getInstance(application);
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
     * Add a menu item to the popup menu.
     * 
     * @param menuItem
     *            A <code>JMenuItem</code>.
     */
    public void add(final JMenuItem menuItem) {
        jPopupMenu.add(menuItem);
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
        jPopupMenu.add(itemFactory.createPopupItem(actionIds, dataList,
                mainActionIndex));
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
     * Show the popup. Note that if the menu is null; or contains no elements;
     * nothing is done.
     * 
     */
    public void show() {
        if (jPopupMenu.getComponentCount() > 0) {
            logger.logApiId();
            logger.logVariable("invoker", invoker);
            logger.logVariable("x", x);
            logger.logVariable("y", y);
            jPopupMenu.show(invoker, x, y);
            invoker = jPopupMenu = null;
            submenus.clear();
            x = y = -1;
        }
    }

    /**
     * Set the menu background type.
     * 
     * @param menuBackgroundType
     *            A <code>MenuBackgroundType</code>.
     */
    protected void setMenuBackgroundType(final MenuBackgroundType menuBackgroundType) {
        if (jPopupMenu instanceof ThinkParityPopupMenu) {
            ((ThinkParityPopupMenu)jPopupMenu).setMenuBackgroundType(menuBackgroundType);
        }
    }
}
