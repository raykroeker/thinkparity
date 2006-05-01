/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.application.system.tray;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;
import com.thinkparity.browser.application.system.SystemApplication;

/**
 * A menu builder for the system application tray.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class TrayMenuBuilder {

    /** System application. */
    private final SystemApplication application;

    final Action login;

    final Action logout;

    /** Create a TrayMenuBuilder. */
    TrayMenuBuilder(final SystemApplication application) {
        super();
        this.application = application;
        this.login = new AbstractAction(getString("Menu.Login")) {

            private static final long serialVersionUID = 1;

            public void actionPerformed(final ActionEvent e) {
                application.runLogin();
            }
        };
        this.login.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.LoginMnemonic").charAt(0)));
        this.logout = new AbstractAction(getString("Menu.Logout")) {

            private static final long serialVersionUID = 1;

            public void actionPerformed(final ActionEvent e) {
                application.runLogout();
            }
        };
        this.logout.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.LogoutMnemonic").charAt(0)));
    }

    /**
     * Create the system tray popup menu.
     * 
     * @return The system tray popup menu.
     */
    JPopupMenu createPopup() {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(createMenuItem(
                "Menu.Browser",
                new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        application.runRestoreBrowser();
                    }}));
        jPopupMenu.addSeparator();
        jPopupMenu.add(login);
        jPopupMenu.add(logout);
        jPopupMenu.add(createMenuItem(
                "Menu.About",
                new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        application.displayAbout();
                    }
                }));
        jPopupMenu.addSeparator();
        jPopupMenu.add(createMenuItem(
                "Menu.Exit",
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        application.runExitPlatform();
                    }
                }));

        return jPopupMenu;
    }

    /**
     * Create a menu item.
     * 
     * @param textKey
     *            The text key.
     * @param l
     *            The action listener.
     * @return The menu item.
     */
    private JMenuItem createMenuItem(final String textKey,
            final ActionListener l) {
        return MenuItemFactory.create(
                getString(textKey),
                new Integer(getString(textKey + "Mnemonic").charAt(0)),
                l);
    }

    /**
     * Obtain localised text.
     *
     * @param localKey
     *      The local context key.
     * @return Localised text.
     */
    private String getString(final String localKey) {
        return application.getString(localKey);
    }
}
