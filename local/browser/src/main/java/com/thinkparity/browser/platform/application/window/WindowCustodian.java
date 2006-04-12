/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.platform.application.window;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/**
 * A window custodian is responsible for disposing of a window.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class WindowCustodian {

    /** The action map key. */
    private static final String ACTION_MAP_KEY_ESCAPE = "actionMapKeyEscape";

    /**
     * Create a WindowCustodian.
     * 
     */
    WindowCustodian() { super(); }

    /**
     * Add a key-map action pair to monitor the escape key; and dispose of
     * the window when pressed.
     *
     */
    void applyEscapeListener(final Window window) {
        window.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), ACTION_MAP_KEY_ESCAPE);
        window.getRootPane().getActionMap().put(ACTION_MAP_KEY_ESCAPE, new AbstractAction() {
            private static final long serialVersionUID = 1;
            public void actionPerformed(final ActionEvent e) {
                window.disposeWindow();
            }});
    }
}
