/*
 * Created On:  4-Dec-06 12:08:27 PM
 */
package com.thinkparity.ophelia.browser.platform.action;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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

    /** A keyboard redispatch <code>Boolean</code>. */
    private Boolean keyboardRedispatchEnabled;

    /** A <code>KeyEventDispatcher</code>. */
    private KeyEventDispatcher keyEventDispatcher;

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
        this.keyboardRedispatchEnabled = Boolean.FALSE;
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
     * Hide the popup.
     */
    public void hide() {
        if (isVisible()) {
            jPopupMenu.setVisible(false);
        }
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
     * Determine if the popup is visible (currently being displayed).
     * 
     * @return true if the popup is visible.
     */
    public Boolean isVisible() {
        return (null != jPopupMenu && jPopupMenu.isVisible());
    }

    /**
     * Enable or disable keyboard redispatch.
     * 
     * When enabled, keyboard keys are intercepted and redispatched to the popup.
     * This is normally not necessary unless the popup is designed to
     * work when it doesn't have focus.
     * 
     * @param keyboardRedispatchEnabled
     *            A <code>Boolean</code>, true to enable keyboard redispatch.
     */
    public void setKeyboardRedispatchEnabled(final Boolean keyboardRedispatchEnabled) {
        this.keyboardRedispatchEnabled = keyboardRedispatchEnabled;
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
            jPopupMenu.addPopupMenuListener(new PopupMenuListener() {
                public void popupMenuCanceled(final PopupMenuEvent e) {}
                public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
                    if (isKeyboardRedispatchEnabled()) {
                        removeKeyEventDispatcher();
                    }
                    jPopupMenu.removePopupMenuListener(this);
                    jPopupMenu = null;
                }
                public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
                    if (isKeyboardRedispatchEnabled()) {
                        addKeyEventDispatcher();
                    }
                }
            });
            jPopupMenu.show(invoker, x, y);
            invoker = null;
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

    /**
     * Add the key event dispatcher.
     * This logic forwards keyboard events to a popup that does not have focus.
     */
    private void addKeyEventDispatcher() {
        removeKeyEventDispatcher();
        keyEventDispatcher = new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(final KeyEvent e) {
                // Handle the enter key as a special case.
                // Simply forwarding VK_ENTER to the menu does not work when the menu
                // does not have focus; instead, call doClick() on the armed JMenuItem.
                if (KeyEvent.KEY_TYPED == e.getID() && KeyEvent.VK_ENTER == e.getKeyChar()) {
                    final Component[] components = jPopupMenu.getComponents();
                    for (final Component component : components) {
                        if (component instanceof JMenuItem) {
                            if (((JMenuItem)component).isArmed()) {
                                ((JMenuItem)component).doClick();
                                break;
                            }
                        }
                    }
                    return true;
                }
                // suck up other VK_ENTER key events
                else if ((KeyEvent.KEY_PRESSED == e.getID() && KeyEvent.VK_ENTER == e.getKeyCode()) ||
                        (KeyEvent.KEY_RELEASED == e.getID() && KeyEvent.VK_ENTER == e.getKeyCode())) {
                    return true;
                }
                // forward escape, up, and down keys
                else if (KeyEvent.KEY_PRESSED == e.getID() || KeyEvent.KEY_RELEASED == e.getID()) {
                    final int keyCode = e.getKeyCode();
                    if (keyCode == KeyEvent.VK_ESCAPE
                            || keyCode == KeyEvent.VK_UP
                            || keyCode == KeyEvent.VK_DOWN) {
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(jPopupMenu, e);
                        return true;
                    }
                }

                return false;
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
    }

    /**
     * Determine if keyboard redispatch is enabled.
     * 
     * @return true if keyboard redispatch is enabled.
     */
    private Boolean isKeyboardRedispatchEnabled() {
        return keyboardRedispatchEnabled;
    }

    /**
     * Remove the key event dispatcher.
     */
    private void removeKeyEventDispatcher() {
        if (null != keyEventDispatcher) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(keyEventDispatcher);
        }
        keyEventDispatcher = null;
    }
}
