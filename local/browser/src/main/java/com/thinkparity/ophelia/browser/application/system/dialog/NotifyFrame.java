/*
 * NotifyFrame.java
 *
 * Created on November 22, 2006, 4:11 PM
 */

package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.AWTException;

import com.thinkparity.ophelia.browser.BrowserException;

/**
 *
 * @author  raymond
 */
public class NotifyFrame extends SystemFrame {

    /** The number of pixels to adjust the y location of the panel by when animating. */
    private static final int ANIMATION_Y_LOCATION_ADJUSTMENT;

    /** The <code>NotifyFrame</code> singleton instance. */
    private static NotifyFrame frame;

    static {
        ANIMATION_Y_LOCATION_ADJUSTMENT = -3;
    }

    /**
     * Create NotifyFrame.
     * 
     */
    private NotifyFrame() throws AWTException {
        super();
        panel = new NotifyPanel();
        initComponents();
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
     * Close notifications.
     * 
     * @param notificationId
     *            A notification id or group id <code>String</code>.
     */
    public static void close(final String notificationId) {
        if (isDisplayed()) {
            frame.doClose(notificationId);
        }
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
     * @see com.thinkparity.ophelia.browser.application.system.dialog.SystemFrame#getAnimationAdjustmentY()
     */
    @Override
    protected int getAnimationAdjustmentY() {
        return ANIMATION_Y_LOCATION_ADJUSTMENT;
    }

    /**
     * Close a notification.
     * 
     * @param notificationId
     *            A notification id or group id <code>String</code>.
     */
    private void doClose(final String notificationId) {
        ((NotifyPanel)panel).close(notificationId);
    }

    /**
     * Display a notification.
     * 
     * @param notification
     *            A <code>Notification</code>.
     */
    private void doDisplay(final Notification notification) {
        // The call to pack() ensures that components in the
        // panel are displayable. See NotifyPanel#reloadNotificationTitle.
        pack();
        ((NotifyPanel)panel).display(notification);
    }
}
