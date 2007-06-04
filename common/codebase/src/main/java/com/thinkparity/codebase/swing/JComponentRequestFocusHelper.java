/**
 * Created On: 4-Jun-07 11:31:02 AM
 * $Id$
 */
package com.thinkparity.codebase.swing;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;


/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
final class JComponentRequestFocusHelper {

    /**
     * A client property key <code>String</code> used to store the mouse
     * adapter within the component.
     */
    private static final String LISTENER_CLIENT_PROPERTY_KEY;

    static {
        LISTENER_CLIENT_PROPERTY_KEY = new StringBuffer(JComponentRequestFocusHelper.class
                .getName()).append("#mouseListener").toString();
    }

    /**
     * Create JComponentRequestFocusHelper.
     * 
     * @param jFrame
     *            An <code>AbstractJFrame</code>.
     */
    JComponentRequestFocusHelper(final AbstractJFrame jFrame) {
        this();
    }

    /**
     * Create JComponentRequestFocusHelper.
     * 
     * @param jPanel
     *            An <code>AbstractJPanel</code>.
     */
    JComponentRequestFocusHelper(final AbstractJPanel jPanel) {
        this();
    }

    /**
     * Create JComponentRequestFocusHelper.
     *
     */
    private JComponentRequestFocusHelper() {
        super();
    }

    /**
     * Add a request focus listener to the component.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    void addListener(final JComponent jComponent) {
        // property to protect against adding it twice
        MouseListener mouseListener =
            (MouseListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null == mouseListener) {
            mouseListener = new MouseAdapter() {
                @Override
                public void mousePressed(final MouseEvent e) {
                    jComponent.requestFocusInWindow();
                }
            };
            jComponent.addMouseListener(mouseListener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, mouseListener);
        }
    }

    /**
     * Remove a request focus listener from the component.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    void removeListener(final JComponent jComponent) {
        MouseListener mouseListener =
            (MouseListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null != mouseListener) {
            jComponent.removeMouseListener(mouseListener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, null);
            mouseListener = null;
        }    
    }
}
