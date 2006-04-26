/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.platform.action;

import javax.swing.ImageIcon;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QuitApplication extends AbstractAction {

    private static final ImageIcon ICON;

    private static final ActionId ID;

    private static final String NAME;

    static {
        ICON = null;
        ID = ActionId.APPLICATION_QUIT;
        NAME = "Quit";
    }

    /** Create a QuitApplication. */
    public QuitApplication() { super("QuitApplication", ID, NAME, ICON); }

    /**
     * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) throws Exception {
        throw Assert.createNotYetImplemented("QuitApplication#invoke");
    }

}
