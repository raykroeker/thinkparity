/**
 * Created On: 9-Oct-07 5:28:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.AWTException;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;

/**
 * @author raymond@thinkparity.com, robert@thinkparity.com
 * @version $Revision$
 */
public class NotifyFrame extends SystemFrame {

    /** The number of pixels to adjust the y location of the panel by when animating. */
    private static final int ANIMATION_Y_LOCATION_ADJUSTMENT;

    /** The <code>NotifyFrame</code> singleton instance. */
    private static NotifyFrame frame;

    static {
        ANIMATION_Y_LOCATION_ADJUSTMENT = -5;
    }

    /**
     * Close the notification frame.
     */
    public static void close() {
        if (isDisplayed()) {
            frame.dispose();
            frame = null;
        }
    }

    /**
     * Close a notification.
     * 
     * @param notificationId
     *            A notification id <code>String</code>.
     */
    public static void close(final String notificationId) {
        if (isDisplayed()) {
            frame.doClose(notificationId);
        }
    }

    /**
     * Test the notification display.
     * 
     * @param notification
     *            A <code>Notification</code>.
     */
    static void testDisplay(final Notification notification,
            final SystemApplication systemApplication) {
        if (null == frame) {
            try {
                frame = new NotifyFrame(systemApplication);
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
     * if the notification panel is not already visible it is set to visible.
     * 
     * @param notification
     *            A <code>Notification</code> to display.
     * @param systemApplication
     *            The <code>SystemApplication</code>.     
     */
    public static void display(final Notification notification,
            final SystemApplication systemApplication) {
        if (null == frame) {
            try {
                frame = new NotifyFrame(systemApplication);
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
     * Fire a connection offline event.
     */
    public static void fireConnectionOffline() {
        if (isFrame()) {
            frame.reloadConnection();
        }
    }

    /**
     * Fire a connection offline event.
     */
    public static void fireConnectionOnline() {
        if (isFrame()) {
            frame.reloadConnection();
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
     * Determine whether or not the frame has been created.
     * 
     * @return True if the frame has been created.
     */
    private static Boolean isFrame() {
        return null != frame;
    }

    /**
     * Create NotifyFrame.
     * 
     * @param systemApplication
     *            The <code>SystemApplication</code>. 
     */
    private NotifyFrame(final SystemApplication systemApplication) throws AWTException {
        super();
        panel = new NotifyPanel();
        initComponents();
        registerPages(systemApplication);
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
     *            A notification id <code>String</code>.
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
        ((NotifyPanel)panel).display(notification);
    }

    /**
     * Register pages.
     * 
     * @param systemApplication
     *            The <code>SystemApplication</code>. 
     */
    private void registerPages(final SystemApplication systemApplication) {
        ((NotifyPanel)panel).registerPage(new ContainerPublishedNotifyPage(systemApplication));
        ((NotifyPanel)panel).registerPage(new InvitationNotifyPage(systemApplication));
        ((NotifyPanel)panel).registerPage(new ProductInstalledNotifyPage(systemApplication));
        ((NotifyPanel)panel).registerPage(new ProfilePassivatedNotifyPage(systemApplication));
    }

    /**
     * Reload the connection status.
     */
    private void reloadConnection() {
        ((NotifyPanel)panel).reloadConnection();
    }
}
