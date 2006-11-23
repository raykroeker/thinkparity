/*
 * Created On: Nov 23, 2006 8:13:10 AM
 */
package com.thinkparity.codebase.swing;

import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class AbstractJPanelMoveHelper {

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /** The location of the mouse pressed event. */
    private Point mousePressed;

    /** The offset x and y coordinates the mouse was dragged. */
    private int offsetX, offsetY;

    /**
     * Create AbstractJPanelMoveHelper.
     * 
     * @param jPanel
     *            A <code>JPanel</code>.
     */
    AbstractJPanelMoveHelper(final JPanel jPanel) {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * Add a move listener to the compent.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    void addListener(final JComponent jComponent) {
        final MouseInputListener mouseInputListener = new MouseInputAdapter() {
            @Override
            public void mouseDragged(final MouseEvent e) {
                jComponentMouseDragged(e);
            }
            @Override
            public void mousePressed(final MouseEvent e) {
                jComponentMousePressed(e);
            }
        };
        jComponent.addMouseMotionListener(mouseInputListener);
        jComponent.addMouseListener(mouseInputListener);
    }

    /**
     * Debug the move helper's state.
     *
     */
    void debug() {
        logger.logVariable("mousePressed", mousePressed);
        logger.logVariable("offsetX", offsetX);
        logger.logVariable("offsetY", offsetY);
    }

    /**
     * The component's mouse dragged event was fired.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jComponentMouseDragged(final MouseEvent e) {
        offsetX = e.getX() - mousePressed.x;
        offsetY = e.getY() - mousePressed.y;
        moveWindow(e);
    }

    /**
     * The component's mouse pressed event was fired.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jComponentMousePressed(final MouseEvent e) {
        mousePressed = e.getPoint();
    }

    /**
     * Move the window.
     * 
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void moveWindow(final MouseEvent e) {
        debug();
        if (offsetX != 0 || offsetY != 0) {
            final Window window =
                SwingUtilities.getWindowAncestor(((JComponent) e.getSource()));
            final Point location = window.getLocation();
            location.x += offsetX;
            location.y += offsetY;
            window.setLocation(location);
        }
    }
}
