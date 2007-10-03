/**
 * Created On: Mar 14, 2007 10:26:56 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.AWTException;

import com.thinkparity.ophelia.browser.BrowserException;
import com.thinkparity.ophelia.browser.application.system.SystemApplication;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DisplayInfoFrame extends SystemFrame {

    /** The number of pixels to adjust the y location of the panel by when animating. */
    private static final int ANIMATION_Y_LOCATION_ADJUSTMENT;

    /** The <code>DisplayInfoFrame</code> singleton instance. */
    private static DisplayInfoFrame frame;

    static {
        ANIMATION_Y_LOCATION_ADJUSTMENT = -5;
    }

    /**
     * @throws AWTException
     */
    public DisplayInfoFrame(final SystemApplication systemApplication) throws AWTException {
        super();
        panel = new DisplayInfoPanel(systemApplication);
        initComponents();
    }

    /**
     * Test the display info frame display.
     * 
     * @param systemApplication
     *            The system application.
     */
    static void testDisplay(final SystemApplication systemApplication) {
        if (null == frame) {
            try {
                frame = new DisplayInfoFrame(systemApplication);
                frame.setLocation(frame.getFinalLocation());
            } catch (final AWTException awtx) {
                throw new BrowserException("Could not instantiate Display Info dialogue.", awtx);
            }
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
     * Display a display info frame.
     * 
     * @param systemApplication
     *            The system application.
     */
    public static void display(final SystemApplication systemApplication) {
        if (null == frame) {
            try {
                frame = new DisplayInfoFrame(systemApplication);
                frame.setLocation(frame.getStartLocation());
            } catch (final AWTException awtx) {
                throw new BrowserException("Could not instantiate Display Info dialogue.", awtx);
            }
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
     * Determine whether or not the frame is being displayed.
     * 
     * @return True if the frame is currently being displayed.
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
}
