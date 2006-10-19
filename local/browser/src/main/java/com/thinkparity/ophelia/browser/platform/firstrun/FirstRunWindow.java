/*
 * Created on June 10, 2006, 11:17 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.firstrun;

import com.thinkparity.ophelia.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.ophelia.browser.util.swing.OpheliaJFrame;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.3
 */
final class FirstRunWindow extends OpheliaJFrame {

    /**
     * Create FirstRunWindow.
     *
     */
    public FirstRunWindow() {
        super("FirstRunWindow");
        setResizable(false);
    }

    /**
     * Add an avatar to the profile window.
     * 
     * @param avatar
     *            A thinkParity <code>Avatar</code>.
     */
    void add(final Avatar avatar) {
        super.add(avatar);
        avatar.reload();
    }
}
