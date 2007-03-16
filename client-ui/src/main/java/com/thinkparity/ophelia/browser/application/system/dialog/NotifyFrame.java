/*
 * NotifyFrame.java
 *
 * Created on November 22, 2006, 4:11 PM
 */

package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.AWTException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

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

    /** The <code>NotifyPanel</code>. */
    private NotifyPanel panel;

    /**
     * Create NotifyFrame.
     * 
     */
    private NotifyFrame() throws AWTException {
        super();
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
     * Reload the notifications.
     *
     */
    private void doDisplay(final Notification notification) {
        // The call to pack() ensures that components in the
        // panel are displayable. See NotifyPanel#reloadNotificationTitle.
        pack();
        panel.display(notification);
    }

    /**
     * Initialize components.
     */
    private void initComponents() {
        panel = new NotifyPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setAlwaysOnTop(true);
        setUndecorated(true);
        setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0F;
        constraints.weighty = 1.0F;
        constraints.gridx = GridBagConstraints.RELATIVE;
        constraints.gridy = GridBagConstraints.RELATIVE;
        add(panel, constraints);
        pack();
    }
}
