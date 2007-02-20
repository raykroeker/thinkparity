/*
 * LoginWindow.java
 *
 * Created on October 18, 2006, 6:30 PM
 */

package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.platform.application.window.WindowPanel;
import com.thinkparity.ophelia.browser.platform.application.window.WindowTitle;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 *
 * @author  raymond
 */
public class LoginWindow extends OpheliaJFrame {

    /** The login avatar. */
    private LoginAvatar loginAvatar;

    /** The panel onto which all displays are dropped. */
    private WindowPanel windowPanel;

    /** Creates new form LoginWindow */
    public LoginWindow(final String username, final String password) {
        super(null);
        loginAvatar = new LoginAvatar(username, password);
        windowPanel = new WindowPanel();
        windowPanel.getWindowTitle().setBorderType(WindowTitle.BorderType.WINDOW_BORDER2);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        initComponents(loginAvatar);
        pack();
    }

    String getUsername() {
        return loginAvatar.getUsername();
    }

    String getPassword() {
        return loginAvatar.getPassword();
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
