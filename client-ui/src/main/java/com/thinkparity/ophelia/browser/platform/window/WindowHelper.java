/*
 * Created On:  4-May-07 2:42:24 PM
 */
package com.thinkparity.ophelia.browser.platform.window;

import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * <b>Title:</b>thinkParity OpheliaUI Platform Window Helper<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class WindowHelper {

    /** An escape action binding between the key stroke and the action. */
    private static final String ACTION_MAP_KEY_ESCAPE;

    static {
        ACTION_MAP_KEY_ESCAPE = "WindowHelper#ACTION_MAP_KEY_ESCAPE";
    }

    /**
     * Bind the escape key to an action.
     * 
     * @param window
     *            A <code>Window</code>.
     * @param action
     *            A swing <code>Action</code>.
     */
    static void bindEscape(final Window window, final Action action) {
        window.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ACTION_MAP_KEY_ESCAPE);
        window.getRootPane().getActionMap().put(ACTION_MAP_KEY_ESCAPE, action);
    }

    /**
     * Create WindowHelper.
     *
     */
    private WindowHelper() {
        super();
    }
}
