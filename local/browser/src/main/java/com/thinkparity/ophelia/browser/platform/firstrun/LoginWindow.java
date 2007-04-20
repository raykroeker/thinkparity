/*
 * Created On:  October 18, 2006, 6:30 PM
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.codebase.model.session.Credentials;

import com.thinkparity.ophelia.model.workspace.InitializeMediator;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowPanel;
import com.thinkparity.ophelia.browser.platform.application.window.WindowTitle;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * <b>Title:</b>thinkParity OpheliaUI Login Window<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
public final class LoginWindow extends OpheliaJFrame {

    /** The login avatar. */
    private LoginAvatar loginAvatar;

    /** The panel onto which all displays are dropped. */
    private WindowPanel windowPanel;

    /**
     * Create LoginWindow.
     * 
     */
    public LoginWindow() {
        super("LoginWindow");
        loginAvatar = new LoginAvatar();
        windowPanel = new WindowPanel();
        windowPanel.getWindowTitle().setBorderType(WindowTitle.BorderType.WINDOW_BORDER2);
        initComponents(loginAvatar);
        loginAvatar.reload();
        pack();
    }

    /**
     * Set the credentials.
     * 
     * @param credentials
     *            The <code>Credentials</code>.
     */
    public void setCredentials(final Credentials credentials) {
        loginAvatar.setCredentials(credentials);
    }

    /**
     * Set the initialize mediator.
     * 
     * @param initializeMediator
     *            The <code>InitializeMediator</code>.
     */
    public void setInitializeMediator(final InitializeMediator initializeMediator) {
        loginAvatar.setInitializeMediator(initializeMediator);
    }

    /**
     * Login (so no button press is required).
     */
    public void login() {
        loginAvatar.login();
    }

    /**
     * Initialize the swing components on the window.
     * 
     * @param avatar
     *            The avatar.
     */
    private void initComponents(final Avatar avatar) {
        windowPanel.addPanel(avatar, Boolean.TRUE);
        add(windowPanel);
    }
}
