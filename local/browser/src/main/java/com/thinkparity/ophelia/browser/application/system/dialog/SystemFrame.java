/**
 * Created On: Mar 14, 2007 10:46:15 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import com.thinkparity.codebase.swing.AbstractJDialog;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder2;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class SystemFrame extends AbstractJDialog {

    /** The system frame's <code>FrameAnimator</code>. */
    private final FrameAnimator frameAnimator;

    /**
     * Create AnimatedFrame.
     * 
     */
    protected SystemFrame() throws AWTException {
        super(null, Boolean.FALSE, "");
        this.frameAnimator = new FrameAnimator(this);
        getRootPane().setBorder(new WindowBorder2());
    }

    /**
     * Get the animation adjustment in the Y direction (the number of pixels
     * to increase the height).
     * 
     * @return The Y animation adjustment <code>int</code>.
     */
    protected abstract int getAnimationAdjustmentY();

    /**
     * Get the final location for the frame.
     * This will be bottom right, accounting for the location of the taskbar.
     * 
     * @return The final location <code>Point</code> for the frame.
     */
    protected Point getFinalLocation() {
        final Rectangle maxBounds = SwingUtil.getPrimaryDesktopBounds();  
        final Point location = new Point(
                maxBounds.x + maxBounds.width - getFinalSize().width - 1,
                maxBounds.y + maxBounds.height - getFinalSize().height - 1);
        return location;
    }

    /**
     * Get the final size for the frame.
     * 
     * @return The final size <code>Dimension</code> for the frame.
     */
    protected Dimension getFinalSize() {
        return getPreferredSize();
    }

    /**
     * Get the start location for the frame.
     * This will be bottom right, accounting for the location of the taskbar.
     * 
     * @return The start location <code>Point</code> for the frame.
     */
    protected Point getStartLocation() {
        final Rectangle maxBounds = SwingUtil.getPrimaryDesktopBounds();
        final Point location = new Point(
                maxBounds.x + maxBounds.width - getFinalSize().width - 1,
                maxBounds.y + maxBounds.height);
        return location;
    }

    /**
     * Get the start size for the frame.
     * 
     * @return The start size <code>Dimension</code> for the frame.
     */
    protected Dimension getStartSize() {
        return new Dimension(getFinalSize().width, 3);
    }

    /**
     * Show the frame.
     * 
     * @param animate
     *            Animate <code>Boolean</code>.
     */
    protected void show(final Boolean animate) {
        if (animate) {
            setLocation(getStartLocation());
            setSize(getStartSize()); 
            validate();
            frameAnimator.slideIn(getAnimationAdjustmentY(),
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
}
