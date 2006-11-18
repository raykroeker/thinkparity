/*
 * Created On:  17-Nov-06 6:32:43 PM
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.thinkparity.ophelia.browser.Constants.Images;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
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
    protected void paintIndeterminate(final Graphics g, final JComponent c) {
    }

    /**
     * @see javax.swing.plaf.basic.BasicProgressBarUI#paintDeterminate(java.awt.Graphics, javax.swing.JComponent)
     *
     */
    @Override
    protected void paintDeterminate(final Graphics g, final JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }
        Insets b = progressBar.getInsets(); // area for border
        int barRectWidth = progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
        // amount of progress to draw
        int amountFull = getAmountFull(b, barRectWidth, barRectHeight);
        final Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(Images.PROGRESS_BAR, b.left, (barRectHeight / 2) + b.top,
                amountFull + b.left, (barRectHeight / 2) + b.top, null);
    }
}
