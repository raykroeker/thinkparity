/*
 * NotifyFrame.java
 *
 * Created on November 22, 2006, 4:11 PM
 */

package com.thinkparity.ophelia.browser.application.system.notify;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.thinkparity.codebase.swing.AbstractJDialog;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.Constants.Dimensions;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder2;
import com.thinkparity.ophelia.browser.util.l2fprod.NativeSkin;

/**
 *
 * @author  raymond
 */
public class NotifyFrame extends AbstractJDialog {

    /** The number of pixels to adjust the y location of the panel by when animating. */
    private static final int ANIMATION_Y_LOCATION_ADJUSTMENT;

    /** The <code>NotifyFrame</code> singleton instance. */
    private static NotifyFrame frame;

    /** The notify frame's <code>NotifyAnimator</code>. */
    private final NotifyAnimator notifyAnimator;

    static {
        ANIMATION_Y_LOCATION_ADJUSTMENT = -3;
    }

    /**
     * Test the notification display.
     * 
     * @param notification
     *            A <code>Notification</code>.
     */
    static void testDisplay(final Notification notification) {
        if (null == frame) {
            try {
                frame = new NotifyFrame();
                frame.setLocation(frame.getFinalLocation());
            } catch (final AWTException awtx) {
                throw new BrowserException("Could not instantiate notification dialogue.", awtx);
            }
        }
        synchronized (notification) {
            frame.doDisplay(notification);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (frame.isVisible())
                    frame.toFront();
                else
                    frame.setVisible(true);
            }
        });
    }

    /**
     * Display a notification. The notification is added to an internal list and
     * if the notify panel is not already visible it is set to visible.
     * 
     * @param notification The notification to display.
     */
    public static void display(final Notification notification) {
        if (null == frame) {
            try {
                frame = new NotifyFrame();
                frame.setLocation(frame.getStartLocation());
            } catch (final AWTException awtx) {
                throw new BrowserException("Could not instantiate notification dialogue.", awtx);
            }
        }
        synchronized (notification) {
            frame.doDisplay(notification);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (frame.isVisible()) {
                    frame.toFront();
                } else {
                    frame.setLocation(frame.getStartLocation());
                    frame.setAlwaysOnTop(false);
                    frame.setVisible(true);
                    frame.toFront();
                    frame.notifyAnimator.slideIn(ANIMATION_Y_LOCATION_ADJUSTMENT,
                            frame.getFinalLocation().y, new Runnable() {
                                public void run() {
                                    frame.setAlwaysOnTop(true);
                                }
                    });
                }
            }
        });
    }

    /**
     * Close the notification.
     *
     */
    public static void close() {
        if (isDisplayed()) {
            frame.dispose();
            frame = null;
        }
    }

    /**
     * Determine whether or not the notification frame is being displayed.
     * 
     * @return True if the notification frame is currently being displayed.
     */
    public static Boolean isDisplayed() {
        return null != frame && frame.isVisible();
    }

    /**
     * Create NotifyFrame.
     * 
     */
    private NotifyFrame() throws AWTException {
        super(null, Boolean.FALSE, "");
        this.notifyAnimator = new NotifyAnimator(this);
        initComponents();
        new NativeSkin().roundCorners(this, Dimensions.BrowserWindow.CORNER_SIZE);
        getRootPane().setBorder(new WindowBorder2());
    }

    /**
     * Reload the notifications.
     *
     */
    private void doDisplay(final Notification notification) {
        notifyPanel.display(notification);
    }

    /**
     * Get the final location for the notify frame.
     * This will be bottom right, accounting for the location of the taskbar.
     * 
     * @return The final location for the frame.
     */
    private Point getFinalLocation() {
        final Point location = getStartLocation();
        location.y -= getSize().height;
        return location;
    }

    /**
     * Get the start location for the notify frame.
     * This will be bottom right, accounting for the location of the taskbar.
     * 
     * @return The start location for the frame.
     */
    private Point getStartLocation() {
        final Rectangle maxBounds = SwingUtil.getPrimaryDesktopBounds();   
        final Dimension windowSize = getSize();
        final Point location = new Point(
                maxBounds.x + maxBounds.width - windowSize.width - 1,
                maxBounds.y + maxBounds.height - 1);
        return location;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        notifyPanel = new com.thinkparity.ophelia.browser.application.system.notify.NotifyPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(notifyPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(notifyPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.thinkparity.ophelia.browser.application.system.notify.NotifyPanel notifyPanel;
    // End of variables declaration//GEN-END:variables
}
