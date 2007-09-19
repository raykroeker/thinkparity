/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.application.system.tray;

import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;

/**
 * A menu builder for the system application tray.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class TrayMenuBuilder {

    /** The browser action. */
    final MenuItem browser;

    /** The display info action. */
    final MenuItem displayInfo;

    /** The exit action. */
    final MenuItem exit;

    /** The restart action. */
    final MenuItem restart;

    /** System application. */
    private final SystemApplication application;

    /** Create a TrayMenuBuilder. */
    TrayMenuBuilder(final SystemApplication application) {
        super();
        this.application = application;
        this.browser = new MenuItem(getString("Menu.Browser"));
        this.browser.addActionListener(new ActionListener() {
            /**
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             *
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                application.runRestoreBrowser();
            }
        });
        this.browser.setFont(Fonts.DefaultFontBold);

        this.displayInfo = new MenuItem(getString("Menu.DisplayInfo"));
        this.displayInfo.addActionListener(new ActionListener() {
            /**
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             *
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                application.displayInfo();
            }
        });

        if(application.isDevelopmentMode()) {
            this.restart = new MenuItem(getString("Menu.Restart"));
            this.restart.addActionListener(new ActionListener() {
                /**
                 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
                 *
                 */
                @Override
                public void actionPerformed(final ActionEvent e) {
                    application.runRestartPlatform();
                }
            });
        } else {
            this.restart = null;
        }

        this.exit = new MenuItem(getString("Menu.Exit"));
        this.exit.addActionListener(new ActionListener() {
            /**
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             *
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                application.runQuitPlatform();
            }
        });
    }

    /**
     * Create the system tray popup menu.
     * 
     * @return The system tray popup menu.
     */
    PopupMenu createPopup() {
        final PopupMenu popupMenu = new PopupMenu();
        popupMenu.setFont(Fonts.DefaultFont);
        popupMenu.add(browser);
        if (application.isDevelopmentMode()) {
            popupMenu.add(restart);
        }
        popupMenu.add(displayInfo);
        popupMenu.addSeparator();
        popupMenu.add(exit);
        return popupMenu;
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
