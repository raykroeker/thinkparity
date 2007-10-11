/**
 * Created On: 9-Oct-07 5:28:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.AWTException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public class PriorityNotifyFrame extends SystemFrame {

    /** The number of pixels to adjust the y location of the panel by when animating. */
    private static final int ANIMATION_Y_LOCATION_ADJUSTMENT;

    /** The <code>PriorityNotifyFrame</code> singleton instance. */
    private static PriorityNotifyFrame frame;

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
     * Display a notification. The notification is added to an internal list and
     * if the notification panel is not already visible it is set to visible.
     * 
     * @param notification
     *            A <code>PriorityNotification</code> to display.
     * @param systemApplication
     *            The <code>SystemApplication</code>.     
     */
    public static void display(final PriorityNotification notification,
            final SystemApplication systemApplication) {
        if (null == frame) {
            try {
                frame = new PriorityNotifyFrame(systemApplication);
                frame.setLocation(frame.getStartLocation());
            } catch (final AWTException awtx) {
                throw new BrowserException("Could not instantiate priority notification dialogue.", awtx);
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
     * Create PriorityNotifyFrame.
     * 
     * @param systemApplication
     *            The <code>SystemApplication</code>. 
     */
    private PriorityNotifyFrame(final SystemApplication systemApplication) throws AWTException {
        super();
        panel = new PriorityNotifyPanel();
        initComponents();
        registerPages(systemApplication);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                systemApplication.firePriorityNotifyWindowClosed();
            }
        });
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
        ((PriorityNotifyPanel)panel).close(notificationId);
    }

    /**
     * Display a notification.
     * 
     * @param notification
     *            A <code>PriorityNotification</code>.
     */
    private void doDisplay(final PriorityNotification notification) {
        ((PriorityNotifyPanel)panel).display(notification);
    }

    /**
     * Register pages.
     * 
     * @param systemApplication
     *            The <code>SystemApplication</code>. 
     */
    private void registerPages(final SystemApplication systemApplication) {
        ((PriorityNotifyPanel)panel).registerPage(new InvitationNotifyPage(systemApplication));
        ((PriorityNotifyPanel)panel).registerPage(new ProductInstalledNotifyPage(systemApplication));
        ((PriorityNotifyPanel)panel).registerPage(new ProfilePassivatedNotifyPage(systemApplication));
    }

    /**
     * Reload the connection status.
     */
    private void reloadConnection() {
        ((PriorityNotifyPanel)panel).reloadConnection();
    }
}
