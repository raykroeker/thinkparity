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

    /** System application. */
    private final SystemApplication application;

    /** The browser action. */
    private final MenuItem browser;

    /** The display info action. */
    private final MenuItem displayInfo;

    /** The update configuration action. */
    private final MenuItem updateConfiguration;

    /** The quit action. */
    private final MenuItem quit;

    /** The restart action. */
    private final MenuItem restart;

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

        this.updateConfiguration = new MenuItem(getString("Menu.UpdateConfiguration"));
        this.updateConfiguration.addActionListener(new ActionListener() {
            /**
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             *
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                application.runUpdateProxyConfiguration();
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

        this.quit = new MenuItem(getString("Menu.Exit"));
        this.quit.addActionListener(new ActionListener() {
            /**
             * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
             *
             */
            @Override
            public void actionPerformed(final ActionEvent e) {
                quit.setEnabled(false);
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
        popupMenu.add(updateConfiguration);
        popupMenu.add(displayInfo);
        popupMenu.addSeparator();
        popupMenu.add(quit);
        return popupMenu;
    }

    /**
     * Set the enabled state of quit.
     * 
     * @param enabled
     *            A <code>Boolean</code>.
     */
    void setQuitEnabled(final Boolean enabled) {
        quit.setEnabled(enabled.booleanValue());
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
