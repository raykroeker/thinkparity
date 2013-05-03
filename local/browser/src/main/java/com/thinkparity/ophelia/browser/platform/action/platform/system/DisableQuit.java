/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.platform.system;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.application.system.SystemApplication;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Ophelia UI System Application Disable Quit Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DisableQuit extends AbstractAction {

    /** The system application. */
    private final SystemApplication system;

    /**
     * Create DisableQuit.
     * 
     * @param system
     *            The <code>SystemApplication</code>.
     */
    public DisableQuit(final SystemApplication system) {
        super(ActionId.SYSTEM_DISABLE_QUIT);
        this.system = system;
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
     * 
     */
    public void invoke(final Data data) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            /**
             * @see java.lang.Runnable#run()
             *
             */
            @Override
            public void run() {
                system.setQuitEnabled(Boolean.FALSE);
            }
        });
    }
}
