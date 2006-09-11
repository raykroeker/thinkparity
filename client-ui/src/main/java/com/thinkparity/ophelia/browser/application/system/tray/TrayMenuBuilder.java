/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.application.system.tray;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;


import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;

/**
 * A menu builder for the system application tray.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class TrayMenuBuilder {

    /** The browser action. */
    final Action browser;

    /** The exit action. */
    final Action exit;

    /** The restart action. */
    final Action restart;

    /** System application. */
    private final SystemApplication application;

    /** Create a TrayMenuBuilder. */
    TrayMenuBuilder(final SystemApplication application) {
        super();
        this.application = application;
        this.browser = new AbstractAction(getString("Menu.Browser")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                application.runRestoreBrowser();
            }};
        this.browser.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.BrowserMnemonic").charAt(0)));

        if(application.isDevelopmentMode()) {
            this.restart = new AbstractAction(getString("Menu.Restart")) {
                private static final long serialVersionUID = 1;
                public void actionPerformed(final ActionEvent e) {
                    application.runRestartPlatform();
                }
            };
            this.restart.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.RestartMnemonic").charAt(0)));
        }
        else { this.restart = null; }

        this.exit = new AbstractAction(getString("Menu.Exit")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                application.runExitPlatform();
            }};
        this.exit.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.ExitMnemonic").charAt(0)));
    }

    /**
     * Create the system tray popup menu.
     * 
     * @return The system tray popup menu.
     */
    JPopupMenu createPopup() {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(browser);
        jPopupMenu.addSeparator();
        if (application.isDevelopmentMode()) {
            jPopupMenu.add(restart);
        }
        jPopupMenu.add(exit);
        return jPopupMenu;
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
