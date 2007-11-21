/*
 * Created On:  Nov 19, 2007 12:18:14 PM
 */
package com.thinkparity.ophelia.support.ui.action.main;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.support.ui.Input;
import com.thinkparity.ophelia.support.ui.action.AbstractAction;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Start extends AbstractAction {

    /**
     * Create Start.
     *
     */
    public Start() {
        super("/main/start");
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.action.Action#invoke(com.thinkparity.ophelia.support.ui.action.Input)
     *
     */
    public void invoke(final Input input) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                try {
                    getContext().lookupWindow("/main/main").setVisible(true);
                } catch (final Throwable t) {
                    t.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        });
    }
}
