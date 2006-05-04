/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.application.system.tray;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.browser.application.browser.component.MenuFactory;
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

    /** The about action. */
    final Action about;

    /** The auto-login action. */
    final Action autoLogin;

    /** The browser action. */
    final Action browser;

    /** The edit profile action. */
    final Action editProfile;

    /** The exit action. */
    final Action exit;

    /** The login action. */
    final Action login;

    /** The logout action. */
    final Action logout;

    /** Create a TrayMenuBuilder. */
    TrayMenuBuilder(final SystemApplication application) {
        super();
        this.application = application;
        this.about = new AbstractAction(getString("Menu.About")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                application.displayAbout();
            }};
        this.about.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.AboutMnemonic").charAt(0)));

        this.autoLogin = new AbstractAction(getString("Menu.AutoLogin")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                application.setAutoLogin(((JCheckBoxMenuItem) e.getSource()).isSelected());
            }};
        this.autoLogin.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.AutoLoginMnemonic").charAt(0)));

        this.browser = new AbstractAction(getString("Menu.Browser")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                application.runRestoreBrowser();
            }};
        this.browser.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.BrowserMnemonic").charAt(0)));
            
        this.editProfile = new AbstractAction(getString("Menu.EditProfile")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
            }};
        this.editProfile.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.EditProfileMnemonic").charAt(0)));

        this.exit = new AbstractAction(getString("Menu.Exit")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                application.runExitPlatform();
            }};
        this.exit.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.ExitMnemonic").charAt(0)));

        this.login = new AbstractAction(getString("Menu.Login")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                application.runLogin();
            }};
        this.login.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.LoginMnemonic").charAt(0)));

        this.logout = new AbstractAction(getString("Menu.Logout")) {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                application.runLogout();
            }};
        this.logout.putValue(Action.MNEMONIC_KEY, new Integer(getString("Menu.LogoutMnemonic").charAt(0)));
    }

    /**
     * Create the system tray popup menu.
     * 
     * @return The system tray popup menu.
     */
    JPopupMenu createPopup() {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(browser);
        jPopupMenu.add(editProfile);
        jPopupMenu.addSeparator();
        jPopupMenu.add(login);
        final JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(autoLogin);
        jCheckBoxMenuItem.setSelected(application.doAutoLogin());
        jPopupMenu.add(jCheckBoxMenuItem);
        jPopupMenu.add(logout);
        jPopupMenu.add(about);
        jPopupMenu.addSeparator();
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
