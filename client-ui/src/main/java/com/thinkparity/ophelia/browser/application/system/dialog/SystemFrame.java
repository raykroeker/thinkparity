/**
 * Created On: Mar 14, 2007 10:46:15 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.border.Border;

import com.thinkparity.codebase.swing.AbstractJDialog;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder2;
import com.thinkparity.ophelia.browser.platform.application.window.WindowBorder2Animating;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public abstract class SystemFrame extends AbstractJDialog {

    /** The <code>Border</code>. */
    final Border border;

    /** The animating <code>Border</code>. */
    final Border borderAnimating;

    /** The system frame's <code>FrameAnimator</code>. */
    private final FrameAnimator frameAnimator;

    /**
     * Create SystemFrame.
     *
     * @throws AWTException
     */
    protected SystemFrame() throws AWTException {
        super(null, Boolean.FALSE, "");
        this.border = new WindowBorder2();
        this.borderAnimating = new WindowBorder2Animating();
        this.frameAnimator = new FrameAnimator(this);
        getRootPane().setBorder(border);
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
     * Account for the difference in height between the two borders. This is
     * done because the border is changed after animation is complete.
     * 
     * @return The final size <code>Dimension</code> for the frame.
     */
    protected Dimension getFinalSize() {
        final Dimension dimension = getPreferredSize();
        dimension.height += getBorderInsetHeight(getFinalBorder())
                - getBorderInsetHeight(getStartBorder());
        return dimension;
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
            getRootPane().setBorder(getStartBorder());
            setLocation(getStartLocation());
            setSize(getStartSize()); 
            validate();
            frameAnimator.slideIn(getAnimationAdjustmentY(),
                    getFinalLocation().y, getFinalSize().height,
                    new Runnable() {
                        public void run() {
                            getRootPane().setBorder(getFinalBorder());
                        }
            });
            setVisible(true);
        } else {
            setLocation(getFinalLocation());
            setVisible(true);
        }
    }

    /**
     * Get the total border inset height.
     * 
     * @return The border inset height <code>int</code>.
     */
    private int getBorderInsetHeight(final Border border) {
        return border.getBorderInsets(this).top + border.getBorderInsets(this).bottom;
    }

    /**
     * Get the final border.
     * 
     * @return The final <code>Border</code>.
     */
    private Border getFinalBorder() {
        return this.border;
    }

    /**
     * Get the start border.
     * 
     * @return The start <code>Border</code>.
     */
    private Border getStartBorder() {
        return this.borderAnimating;
    }
}
