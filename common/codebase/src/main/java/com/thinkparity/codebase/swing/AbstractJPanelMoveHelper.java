/*
 * Created On: Nov 23, 2006 8:13:10 AM
 */
package com.thinkparity.codebase.swing;

import java.awt.Dimension;
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

    /**
     * A client property key <code>String</code> used to store the mouse input
     * adapter within the component.
     */
    private static final String LISTENER_CLIENT_PROPERTY_KEY;

    static {
        LISTENER_CLIENT_PROPERTY_KEY = new StringBuffer(AbstractJPanelMoveHelper.class.getName())
            .append("#mouseInputListener").toString();
    }

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /** The location of the mouse pressed event. */
    private Point mousePressed;

    /** The offset x and y coordinates the mouse was dragged. */
    private int offsetX, offsetY;

    /** The number of pixels from the edge of a screen to interpret as sticky. */
    private final int stickyPixels;

    /**
     * Create AbstractJPanelMoveHelper.
     * 
     * @param jPanel
     *            A <code>JPanel</code>.
     */
    AbstractJPanelMoveHelper(final JPanel jPanel, final int stickyPixels) {
        super();
        this.logger = new Log4JWrapper();
        this.stickyPixels = stickyPixels;
    }

    /**
     * Add a move listener to the compent.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    void addListener(final JComponent jComponent) {
        // property to protect against adding it twice
        MouseInputListener mouseInputListener =
            (MouseInputListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null == mouseInputListener) {
            mouseInputListener = new MouseInputAdapter() {
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
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, mouseInputListener);
        }
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
     * Remove a move listener from the component.
     * 
     * @param jComponent
     *            A <code>JComponent</code>.
     */
    void removeListener(final JComponent jComponent) {
        MouseInputListener mouseInputListener =
            (MouseInputListener) jComponent.getClientProperty(LISTENER_CLIENT_PROPERTY_KEY);
        if (null != mouseInputListener) {
            jComponent.removeMouseMotionListener(mouseInputListener);
            jComponent.removeMouseListener(mouseInputListener);
            jComponent.putClientProperty(LISTENER_CLIENT_PROPERTY_KEY, null);
            mouseInputListener = null;
        }        
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
            final Point newLocation = (Point) location.clone();
            final Dimension size = window.getSize();
            final Dimension primaryScreenSize = SwingUtil.getPrimaryScreenSize();
            newLocation.x += offsetX;
            newLocation.y += offsetY;
            // if the location on the screen is within a given number of pixels;
            // stick to the size of the side of the screen
            if (stickyPixels > newLocation.x) {
                newLocation.x = 0;
            } else if (newLocation.x + size.width > primaryScreenSize.width - stickyPixels) {
                newLocation.x = primaryScreenSize.width - size.width;
            }
            if (stickyPixels > newLocation.y) {
                newLocation.y = 0;
            } else if (newLocation.y + size.height > primaryScreenSize.height - stickyPixels) {
                newLocation.y = primaryScreenSize.height - size.height;
            }
            window.setLocation(newLocation);
            window.firePropertyChange("location.x", Long.valueOf(location.x), Long.valueOf(newLocation.x));
            window.firePropertyChange("location.y", Long.valueOf(location.y), Long.valueOf(newLocation.y));
        }
    }
}
