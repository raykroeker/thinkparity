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
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder2;

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
                    frame.show(Boolean.FALSE);
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
                    frame.show(Boolean.TRUE);
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
        getRootPane().setBorder(new WindowBorder2());
    }

    /**
     * Show the notify frame.
     * 
     * @param animate
     *            Animate <code>Boolean</code>.
     */
    private void show(final Boolean animate) {
        if (animate) {
            setLocation(getStartLocation());
            setSize(getStartSize()); 
            validate();
            notifyAnimator.slideIn(ANIMATION_Y_LOCATION_ADJUSTMENT,
                    getFinalLocation().y, getFinalSize().height,
                    new Runnable() {
                        public void run() {}
            });
            setVisible(true);
        } else {
            setLocation(getFinalLocation());
            setVisible(true);
        }
    }

    /**
     * Reload the notifications.
     *
     */
    private void doDisplay(final Notification notification) {
        // The call to pack() ensures that components in the
        // panel are displayable. See NotifyPanel#reloadNotificationTitle.
        pack();
        notifyPanel.display(notification);
    }

    /**
     * Get the final location for the notify frame.
     * This will be bottom right, accounting for the location of the taskbar.
     * 
     * @return The final location <code>Point</code> for the frame.
     */
    private Point getFinalLocation() {
        final Rectangle maxBounds = SwingUtil.getPrimaryDesktopBounds();  
        final Point location = new Point(
                maxBounds.x + maxBounds.width - getFinalSize().width - 1,
                maxBounds.y + maxBounds.height - getFinalSize().height - 1);
        return location;
    }

    /**
     * Get the final size for the notify frame.
     * 
     * @return The final size <code>Dimension</code> for the frame.
     */
    private Dimension getFinalSize() {
        return getPreferredSize();
    }

    /**
     * Get the start location for the notify frame.
     * This will be bottom right, accounting for the location of the taskbar.
     * 
     * @return The start location <code>Point</code> for the frame.
     */
    private Point getStartLocation() {
        final Rectangle maxBounds = SwingUtil.getPrimaryDesktopBounds();
        final Point location = new Point(
                maxBounds.x + maxBounds.width - getFinalSize().width - 1,
                maxBounds.y + maxBounds.height);
        return location;
    }

    /**
     * Get the start size for the notify frame.
     * 
     * @return The start size <code>Dimension</code> for the frame.
     */
    private Dimension getStartSize() {
        return new Dimension(getFinalSize().width, 3);
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
