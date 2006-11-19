/*
 * Created On:  17-Nov-06 6:32:43 PM
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.thinkparity.ophelia.browser.Constants.Images.ProgressBar;

/**
 * <b>Title:</b>thinkParity Progress Bar UI<br>
 * <b>Description:</b>A custom UI for a thinkParity progress bar. A thinkParity
 * progress bar can be either determinate or indeterminate; however it only
 * supports horizontal operation and the bar must be 24 pixels tall.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ThinkParityProgressBarUI extends BasicProgressBarUI {

    /**
     * Create ThinkParityProgressBarUI.
     *
     */
    public ThinkParityProgressBarUI() {
        super();
    }

    /**
     * @see javax.swing.plaf.basic.BasicProgressBarUI#paintIndeterminate(java.awt.Graphics, javax.swing.JComponent)
     *
     */
    @Override
    protected void paintIndeterminate(Graphics g, JComponent c) {
        final Graphics2D g2 = (Graphics2D) g;
        boxRect = getBox(boxRect);
        if (boxRect != null) {
            g2.drawImage(ProgressBar.ON, boxRect.x, boxRect.y, boxRect.width,
                    boxRect.height, null);
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicProgressBarUI#paintDeterminate(java.awt.Graphics, javax.swing.JComponent)
     *
     */
    @Override
    protected void paintDeterminate(final Graphics g, final JComponent c) {
        final Graphics2D g2 = (Graphics2D) g;
        final Insets insets = progressBar.getInsets();
        final int innerWidth = progressBar.getWidth() - insets.right + insets.left;
        final int innerHeight = progressBar.getHeight() - insets.top + insets.bottom;
        final int x = insets.left;
        final int y = insets.top;
        final int width = getAmountFull(insets, innerWidth, innerHeight);
        final int height = innerHeight;
        g2.drawImage(ProgressBar.ON, x, y, width, height, null);
    }
}
