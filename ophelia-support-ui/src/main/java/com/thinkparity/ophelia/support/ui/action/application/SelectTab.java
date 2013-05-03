/*
 * Created On:  12-Dec-07 11:27:22 AM
 */
package com.thinkparity.ophelia.support.ui.action.application;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.support.ui.Input;
import com.thinkparity.ophelia.support.ui.action.AbstractAction;
import com.thinkparity.ophelia.support.ui.avatar.main.MainAvatar;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SelectTab extends AbstractAction {

    /**
     * Create SelectTab.
     *
     */
    public SelectTab() {
        super("/application/selecttab");
    }

    /**
     * @see com.thinkparity.ophelia.support.ui.action.Action#invoke(com.thinkparity.ophelia.support.ui.Input)
     *
     */
    @Override
    public void invoke(final Input input) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                try {
                    ((MainAvatar) getContext().lookupAvatar("/main/main")).selectApplication();
                } catch (final Throwable t) {
                    t.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        });
    }
}
