/**
 * Created On: 2-Oct-07 12:53:35 PM
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
public class InvitationFrame extends SystemFrame {

    /** The number of pixels to adjust the y location of the panel by when animating. */
    private static final int ANIMATION_Y_LOCATION_ADJUSTMENT;

    /** The <code>InvitationFrame</code> singleton instance. */
    private static InvitationFrame frame;

    static {
        ANIMATION_Y_LOCATION_ADJUSTMENT = -5;
    }

    /**
     * Close the invitation.
     */
    public static void close() {
        if (isDisplayed()) {
            frame.dispose();
            frame = null;
        }
    }

    /**
     * Close invitations.
     * 
     * @param invitationId
     *            An invitation id <code>String</code>.
     */
    public static void close(final String invitationId) {
        if (isDisplayed()) {
            frame.doClose(invitationId);
        }
    }

    /**
     * Display an invitation. The invitation is added to an internal list and
     * if the invitation panel is not already visible it is set to visible.
     * 
     * @param invitation
     *            An <code>Invitation</code> to display.
     * @param systemApplication
     *            The <code>SystemApplication</code>.     
     */
    public static void display(final Invitation invitation,
            final SystemApplication systemApplication) {
        if (null == frame) {
            try {
                frame = new InvitationFrame(systemApplication);
                frame.setLocation(frame.getStartLocation());
            } catch (final AWTException awtx) {
                throw new BrowserException("Could not instantiate invitation dialogue.", awtx);
            }
        }
        synchronized (invitation) {
            frame.doDisplay(invitation);
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
        if (isDisplayed()) {
            frame.reloadConnection();
        }
    }

    /**
     * Fire a connection offline event.
     */
    public static void fireConnectionOnline() {
        if (isDisplayed()) {
            frame.reloadConnection();
        }
    }

    /**
     * Determine whether or not the invitation frame is being displayed.
     * 
     * @return True if the invitation frame is currently being displayed.
     */
    public static Boolean isDisplayed() {
        return null != frame && frame.isVisible();
    }

    /**
     * Create InvitationFrame.
     * 
     * @param systemApplication
     *            The <code>SystemApplication</code>. 
     */
    private InvitationFrame(final SystemApplication systemApplication) throws AWTException {
        super();
        panel = new InvitationPanel(systemApplication);
        initComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                systemApplication.fireInvitationWindowClosed();
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
     * Close an invitation.
     * 
     * @param invitationId
     *            An invitation id <code>String</code>.
     */
    private void doClose(final String invitationId) {
        ((InvitationPanel)panel).close(invitationId);
    }

    /**
     * Display an invitation.
     * 
     * @param invitation
     *            A <code>Invitation</code>.
     */
    private void doDisplay(final Invitation invitation) {
        ((InvitationPanel)panel).display(invitation);
    }

    /**
     * Reload the connection status.
     */
    private void reloadConnection() {
        ((InvitationPanel)panel).reloadConnection();
    }
}
