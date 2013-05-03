/**
 * Created On: 21-Dec-07 10:25:39 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.AWTException;

import com.thinkparity.ophelia.model.workspace.configuration.ProxyConfiguration;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public class UpdateConfigurationFrame extends SystemFrame {

    /** The number of pixels to adjust the y location of the panel by when animating. */
    private static final int ANIMATION_Y_LOCATION_ADJUSTMENT;

    /** The <code>UpdateConfigurationFrame</code> singleton instance. */
    private static UpdateConfigurationFrame frame;

    static {
        ANIMATION_Y_LOCATION_ADJUSTMENT = -5;
    }

    /**
     * Close the frame.
     *
     */
    public static void close() {
        if (isDisplayed()) {
            frame.dispose();
            frame = null;
        }
    }

    /**
     * Display a network settings frame.
     * 
     * @param configuration
     *            A <code>ProxyConfiguration</code>.
     */
    public static void display(final ProxyConfiguration configuration) {
        if (null == frame) {
            try {
                frame = new UpdateConfigurationFrame();
                frame.setLocation(frame.getStartLocation());
            } catch (final AWTException awtx) {
                throw new BrowserException("Could not instantiate Network Settings dialogue.", awtx);
            }
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                if (frame.isVisible()) {
                    frame.toFront();
                } else {
                    frame.reloadConfiguration(configuration);
                    frame.show(Boolean.FALSE);
                }
            }
        });
    }

    /**
     * Determine whether or not the frame is being displayed.
     * 
     * @return True if the frame is currently being displayed.
     */
    public static Boolean isDisplayed() {
        return null != frame && frame.isVisible();
    }

    /**
     * @throws AWTException
     */
    public UpdateConfigurationFrame() throws AWTException {
        super();
        panel = AvatarFactory.create(AvatarId.DIALOG_PLATFORM_UPDATE_CONFIGURATION);
        initComponents();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.SystemFrame#getAnimationAdjustmentY()
     */
    @Override
    protected int getAnimationAdjustmentY() {
        return ANIMATION_Y_LOCATION_ADJUSTMENT;
    }

    /**
     * Reload the configuration.
     * 
     * @param configuration
     *            A <code>ProxyConfiguration</code>.
     */
    private void reloadConfiguration(final ProxyConfiguration configuration) {
        ((UpdateConfigurationAvatar) panel).reloadConfiguration(configuration);
    }
}
