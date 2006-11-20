/*
 * Created On:  17-Nov-06 6:32:43 PM
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * <b>Title:</b>thinkParity Progress Bar UI<br>
 * <b>Description:</b>A custom UI for a thinkParity progress bar. A thinkParity
 * progress bar can be either determinate or indeterminate; however it only
 * supports horizontal operation and the bar must be 24 pixels tall.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ThinkParityProgressBarUI extends BasicProgressBarUI {

    /** The progress bar's off <code>BufferedImage</code>. */
    private static final BufferedImage OFF = ImageIOUtil.read("ProgressBarOffSlice.png");

    /** The progress bar's on <code>BufferedImage</code>. */
    private static final BufferedImage ON = ImageIOUtil.read("ProgressBarOnSlice.png");

    /**
     * Create a thinkParity progress bar ui.
     * 
     * @param c
     *            A <code>JComponent</code>.
     * @return A <code>ComponentUI</code>.
     */
    public static ComponentUI createUI(final JComponent c) {
        return new ThinkParityProgressBarUI();
    }

    /**
     * Create ThinkParityProgressBarUI.
     *
     */
    public ThinkParityProgressBarUI() {
        super();
    }

    /**
     * @see javax.swing.plaf.basic.BasicProgressBarUI#paintDeterminate(java.awt.Graphics, javax.swing.JComponent)
     *
     */
    @Override
    protected void paintDeterminate(final Graphics g, final JComponent c) {
        paintOff(g, c);
        final Graphics2D g2 = (Graphics2D) g;
        final Insets insets = c.getInsets();
        final Dimension size = c.getSize();
        final int innerWidth = size.width - insets.right + insets.left;
        final int innerHeight = size.height - insets.top + insets.bottom;
        final int x = insets.left;
        final int y = insets.top;
        final int width = getAmountFull(insets, innerWidth, innerHeight);
        final int height = innerHeight;
        g2.drawImage(ON, x, y, width, height, null);
    }

    /**
     * @see javax.swing.plaf.basic.BasicProgressBarUI#paintIndeterminate(java.awt.Graphics, javax.swing.JComponent)
     *
     */
    @Override
    protected void paintIndeterminate(final Graphics g, final JComponent c) {
        paintOff(g, c);
        final Graphics2D g2 = (Graphics2D) g;
        boxRect = getBox(boxRect);
        if (boxRect != null) {
            g2.drawImage(ON, boxRect.x, boxRect.y, boxRect.width,
                    boxRect.height, null);
        }
    }

    /**
     * Paint the off portion of the progress bar.
     * 
     * @param g
     *            The <code>Graphics</code>.
     * @param c
     *            The <code>JComponent</code>.
     */
    protected void paintOff(final Graphics g, final JComponent c) {
        final Insets insets = c.getInsets();
        final Dimension size = c.getSize();
        final Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(OFF, insets.left, insets.top,
                size.width - insets.left - insets.right,
                size.height - insets.top - insets.bottom, null);
    }
}
