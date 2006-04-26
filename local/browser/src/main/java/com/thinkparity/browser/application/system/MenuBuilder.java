/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.application.system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.MenuItemFactory;

/**
 * The menu builder for the system application.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class MenuBuilder {

    /** System application. */
    private final SysApp application;

    /**
     * Create a MenuBuilder.
     */
    MenuBuilder(final SysApp application) {
        super();
        this.application = application;
    }

    JPopupMenu createPopup() {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(createMenuItem(
                "Menu.Browser",
                new ActionListener() {
                    public void actionPerformed(final ActionEvent e) {
                        application.runRestoreBrowser();
                    }}));
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
    private String getString(final String localKey) { return application.getString(localKey); }
}
