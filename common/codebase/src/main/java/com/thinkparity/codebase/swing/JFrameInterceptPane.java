/*
 * Created On:  4-May-07 3:33:59 PM
 */
package com.thinkparity.codebase.swing;

import java.awt.event.*;

import javax.swing.JComponent;

/**
 * <b>Title:</b>thinkParity Common JFrame Intercept Pane <br>
 * <b>Description:</b>A panel used to intercept all mouse events on a frame.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class JFrameInterceptPane extends JComponent implements
        KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * Create JFrameInterceptPane.
     * 
     */
    JFrameInterceptPane() {
        super();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(final KeyEvent e) {
        e.consume();
    }

    /**
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    public void keyReleased(final KeyEvent e) {
        e.consume();
    }

    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(final KeyEvent e) {}

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     *
     */
    public void mouseClicked(final MouseEvent e) {}

    /**
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     *
     */
    public void mouseDragged(final MouseEvent e) {}

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     *
     */
    public void mouseEntered(final MouseEvent e) {}

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     *
     */
    public void mouseExited(final MouseEvent e) {}

    /**
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     *
     */
    public void mouseMoved(final MouseEvent e) {}

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     *
     */
    public void mousePressed(final MouseEvent e) {}

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     *
     */
    public void mouseReleased(final MouseEvent e) {}

    /**
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     *
     */
    public void mouseWheelMoved(final MouseWheelEvent e) {}
}