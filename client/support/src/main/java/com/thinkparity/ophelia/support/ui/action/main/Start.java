/*
 * Created On:  Nov 19, 2007 12:18:14 PM
 */
package com.thinkparity.ophelia.support.ui.action.main;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

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
public final class Start extends AbstractAction {

    static {
        System.setProperty("java.net.useSystemProxies", "true");
    }

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
                    // calculate centre screen
                    final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                    final Dimension size = new Dimension(500, 500);
                    final Point location = new Point();
                    location.x = (screen.width - size.width) / 2;
                    location.y = (screen.height - size.height) / 2;

                    getContext().lookupWindow("/main/main").setLocation(location);
                    getContext().lookupWindow("/main/main").setSize(size);
                    getContext().lookupWindow("/main/main").setVisible(true);
                    ((MainAvatar) getContext().lookupAvatar("/main/main")).selectApplication();
                } catch (final Throwable t) {
                    t.printStackTrace(System.err);
                    System.exit(1);
                }
            }
        });
    }
}
