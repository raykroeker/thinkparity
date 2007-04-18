/*
 * Created On:  17-Nov-06 6:32:43 PM
 */
package com.thinkparity.ophelia.browser.util.swing.plaf;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * <b>Title:</b>thinkParity Progress Bar UI<br>
 * <b>Description:</b>A custom UI for a thinkParity progress bar. A thinkParity
 * progress bar can be either determinate or indeterminate; however it only
 * supports horizontal operation and the bar must be 23 pixels tall.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ThinkParityProgressBarUI extends BasicProgressBarUI {

    /** A <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    /** Transparent colour. */
    private static final Color COLOR_TRANSPARENT;
    
    /** The colour for the bottom line. */
    private static final Color COLOR_BOTTOM;

    /** The colour for the top line. */
    private static final Color COLOR_TOP;

    /** The progress bar's on slice <code>BufferedImage</code>. */
    private static final BufferedImage ON;

    /** The progress bar's on slice, at the left and right edges <code>BufferedImage</code>. */
    private static final BufferedImage ON_EDGE;

    /** The <code>BufferedImage</code> for the progress bar bottom. */
    private static final BufferedImage PROGRESS_BAR_BOTTOM;

    /** The <code>BufferedImage</code> for the progress bar bottom left. */
    private static final BufferedImage PROGRESS_BAR_BOTTOM_LEFT;

    /** The <code>BufferedImage</code> for the progress bar bottom right. */
    private static final BufferedImage PROGRESS_BAR_BOTTOM_RIGHT;

    /** The <code>BufferedImage</code> for the progress bar left. */
    private static final BufferedImage PROGRESS_BAR_LEFT;

    /** The <code>BufferedImage</code> for the progress bar middle. */
    private static final BufferedImage PROGRESS_BAR_MIDDLE;

    /** The <code>BufferedImage</code> for the progress bar right. */
    private static final BufferedImage PROGRESS_BAR_RIGHT;

    /** The <code>BufferedImage</code> for the progress bar top. */
    private static final BufferedImage PROGRESS_BAR_TOP;
    
    /** The <code>BufferedImage</code> for the progress bar top left. */
    private static final BufferedImage PROGRESS_BAR_TOP_LEFT;

    /** The <code>BufferedImage</code> for the progress bar top right. */
    private static final BufferedImage PROGRESS_BAR_TOP_RIGHT;

    /** The scaled background image for the progress bar bottom. */
    private BufferedImage scaledProgressBarBottom;  

    /** The scaled background image for the progress bar left. */
    private BufferedImage scaledProgressBarLeft;    

    /** The scaled background image for the progress bar middle. */
    private BufferedImage scaledProgressBarMiddle;

    /** The scaled background image for the progress bar right. */
    private BufferedImage scaledProgressBarRight;

    /** The scaled background image for the progress bar top. */
    private BufferedImage scaledProgressBarTop;

    static {
        LOGGER = new Log4JWrapper();

        COLOR_TRANSPARENT = new Color(0, 0, 0, 0);
        COLOR_BOTTOM = new Color(212, 221, 231, 255);
        COLOR_TOP = new Color(244, 245, 245, 255);
        ON = ImageIOUtil.read("ProgressBarOnSlice.png");
        ON_EDGE = ImageIOUtil.read("ProgressBarOnSliceEdge.png");
        PROGRESS_BAR_BOTTOM = ImageIOUtil.read("ProgressBarBottom.png");
        PROGRESS_BAR_BOTTOM_LEFT = ImageIOUtil.read("ProgressBarBottomLeft.png");
        PROGRESS_BAR_BOTTOM_RIGHT = ImageIOUtil.read("ProgressBarBottomRight.png");
        PROGRESS_BAR_LEFT = ImageIOUtil.read("ProgressBarLeft.png");
        PROGRESS_BAR_MIDDLE = ImageIOUtil.read("ProgressBarMiddle.png");
        PROGRESS_BAR_RIGHT = ImageIOUtil.read("ProgressBarRight.png");
        PROGRESS_BAR_TOP = ImageIOUtil.read("ProgressBarTop.png");
        PROGRESS_BAR_TOP_LEFT = ImageIOUtil.read("ProgressBarTopLeft.png");
        PROGRESS_BAR_TOP_RIGHT = ImageIOUtil.read("ProgressBarTopRight.png");
    }

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
        paintBackground(g, c);
        final Graphics2D g2 = (Graphics2D) g;
        final Insets insets = c.getInsets();
        final Dimension size = c.getSize();
        // Note that one pixel of the top and bottom "borders" are intentionally drawn over.
        final int heightTop = PROGRESS_BAR_TOP.getHeight() - 1;
        final int widthLeft = PROGRESS_BAR_LEFT.getWidth();
        final int heightBottom = PROGRESS_BAR_BOTTOM.getHeight() - 1;
        final int widthRight = PROGRESS_BAR_RIGHT.getWidth();
        final int innerWidth = size.width - insets.right - insets.left - widthRight - widthLeft;
        final int innerHeight = size.height - insets.top - insets.bottom - heightTop - heightBottom;
        final int x = insets.left + widthLeft;
        final int y = insets.top + heightTop;
        final int width = getAmountFull(insets, innerWidth, innerHeight);
        final int height = innerHeight;
        // Left and right edges use special images that are one pixel thick and one
        // pixel inset top and bottom. This preserves the rounded look.
        if (width > 0) {
            g2.drawImage(ON_EDGE, x, y+1, 1, height-2, null);
        }
        if (width == innerWidth) {
            g2.drawImage(ON_EDGE, x+width-1, y+1, 1, height-2, null);
        }
        // Draw the main part of the orange bar, less the leftmost and rightmost pixel
        if (width > 1) {
            final int adjustedWidth = (width == innerWidth ? width-2 : width-1);
            g2.drawImage(ON, x+1, y, adjustedWidth, height, null);
        }
    }

    /**
     * @see javax.swing.plaf.basic.BasicProgressBarUI#paintIndeterminate(java.awt.Graphics, javax.swing.JComponent)
     *
     */
    @Override
    protected void paintIndeterminate(final Graphics g, final JComponent c) {
        paintBackground(g, c);
        final Graphics2D g2 = (Graphics2D) g;
        Rectangle rect = null;
        try {
            rect = getBox(boxRect);
        } catch (final NullPointerException npx) {
            LOGGER.logWarning("A null pointer error has occured within the basic progress bar ui get box implementation.");
        }
        if (rect != null) {
            // Draw the indeterminate box. Limit how far left and right it is allowed
            // to draw to account for borders on left and right.
            final Dimension size = c.getSize();
            final int heightTop = PROGRESS_BAR_TOP.getHeight() - 1;
            final int widthLeft = PROGRESS_BAR_LEFT.getWidth();
            final int heightBottom = PROGRESS_BAR_BOTTOM.getHeight() - 1;
            final int widthRight = PROGRESS_BAR_RIGHT.getWidth();
            if (rect.x < widthLeft) {
                rect.width -= (widthLeft - rect.x);
                rect.x = widthLeft;
            } else if (rect.x + rect.width > size.width - widthRight) {
                rect.width = size.width - rect.x - widthRight;
            }
            rect.y += heightTop;
            rect.height -= (heightTop + heightBottom);
            g2.drawImage(ON, rect.x, rect.y, rect.width, rect.height, null);
        }
    }

    /**
     * Paint the background including the off portion of the progress bar
     * and shadow borders.
     * 
     * @param g
     *            The <code>Graphics</code>.
     * @param c
     *            The <code>JComponent</code>.
     */
    private void paintBackground(final Graphics g, final JComponent c) {
        final Insets insets = c.getInsets();
        final Dimension size = c.getSize();
        scaleImages(g, size.width - insets.left - insets.right,
                size.height - insets.top - insets.bottom);
        final Graphics2D g2 = (Graphics2D)g.create();
        try {
            // Draw the background including the shadow border
            final int heightTop = scaledProgressBarTop.getHeight();
            final int heightMid = scaledProgressBarMiddle.getHeight();
            final int widthLeftRow0 = PROGRESS_BAR_TOP_LEFT.getWidth();
            final int widthMidRow0 = scaledProgressBarTop.getWidth();
            final int widthLeftRow1 = scaledProgressBarLeft.getWidth();
            final int widthMidRow1 = scaledProgressBarMiddle.getWidth();
            final int widthLeftRow2 = PROGRESS_BAR_BOTTOM_LEFT.getWidth();
            final int widthMidRow2 = scaledProgressBarBottom.getWidth();
            g2.drawImage(PROGRESS_BAR_TOP_LEFT,
                    insets.left,
                    insets.top, null);
            g2.drawImage(scaledProgressBarTop,
                    insets.left + widthLeftRow0,
                    insets.top, null);
            g2.drawImage(PROGRESS_BAR_TOP_RIGHT,
                    insets.left + widthLeftRow0 + widthMidRow0,
                    insets.top, null);
            g2.drawImage(scaledProgressBarLeft,
                    insets.left,
                    insets.top + heightTop, null);
            g2.drawImage(scaledProgressBarMiddle,
                    insets.left + widthLeftRow1,
                    insets.top + heightTop, null);
            g2.drawImage(scaledProgressBarRight,
                    insets.left + widthLeftRow1 + widthMidRow1,
                    insets.top + heightTop, null);
            g2.drawImage(PROGRESS_BAR_BOTTOM_LEFT,
                    insets.left,
                    insets.top + heightTop + heightMid, null);
            g2.drawImage(scaledProgressBarBottom,
                    insets.left + widthLeftRow2,
                    insets.top + heightTop + heightMid, null);
            g2.drawImage(PROGRESS_BAR_BOTTOM_RIGHT,
                    insets.left + widthLeftRow2 + widthMidRow2,
                    insets.top + heightTop + heightMid, null);

            // Draw additional border lines
            g2.setPaint(COLOR_TOP);
            g2.drawLine(widthLeftRow1+1, heightTop-1, widthLeftRow1+widthMidRow1-2, heightTop-1);
            g2.setPaint(COLOR_BOTTOM);
            g2.drawLine(widthLeftRow1+1, heightTop+heightMid, widthLeftRow1+widthMidRow1-2, heightTop+heightMid);
        }
        finally { g2.dispose(); }
    }

    /**
     * Scale one image. Smooth interpolation is used because otherwise jaggies are visible.
     * Alpha values are preserved.
     * 
     * @param g
     *            The <code>Graphics</code>.
     * @param input
     *            The input <code>BufferedImage</code>.
     * @param lastScaledImage
     *            The most recent scaled <code>BufferedImage</code>.
     * @param width
     *            The width <code>int</code>.
     * @param height
     *            The height <code>int</code>.
     * @return A scaled <code>BufferedImage</code>.
     */
    private BufferedImage scaleImage(final Graphics g, final BufferedImage input,
            final BufferedImage lastScaledImage,
            final int width, final int height) {
        if ((null == lastScaledImage) || (lastScaledImage.getWidth() != width) || (lastScaledImage.getHeight() != height)) {
            final Image image = input.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            final BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
            final Graphics gImage = output.createGraphics();
            try {
                gImage.drawImage(image, 0, 0, COLOR_TRANSPARENT, null);
            }
            finally { gImage.dispose(); }
            return output;
        } else {
            return lastScaledImage;
        }
    }

    /**
     * Scale images.
     * 
     * @param g
     *            The <code>Graphics</code>. 
     * @param width
     *            The width <code>int</code>.
     * @param height
     *            The height <code>int</code>.
     */
    private void scaleImages(final Graphics g, final int width, final int height) {
        scaledProgressBarBottom = scaleImage(g, PROGRESS_BAR_BOTTOM, scaledProgressBarBottom,
                width - PROGRESS_BAR_BOTTOM_LEFT.getWidth() - PROGRESS_BAR_BOTTOM_RIGHT.getWidth(),
                PROGRESS_BAR_BOTTOM.getHeight());
        scaledProgressBarLeft = scaleImage(g, PROGRESS_BAR_LEFT, scaledProgressBarLeft,
                PROGRESS_BAR_LEFT.getWidth(),
                height - PROGRESS_BAR_TOP_LEFT.getHeight() - PROGRESS_BAR_BOTTOM_LEFT.getHeight());
        scaledProgressBarMiddle = scaleImage(g, PROGRESS_BAR_MIDDLE, scaledProgressBarMiddle,
                width - PROGRESS_BAR_LEFT.getWidth() - PROGRESS_BAR_RIGHT.getWidth(),
                height - PROGRESS_BAR_TOP.getHeight() - PROGRESS_BAR_BOTTOM.getHeight());
        scaledProgressBarRight = scaleImage(g, PROGRESS_BAR_RIGHT, scaledProgressBarRight,
                PROGRESS_BAR_RIGHT.getWidth(),
                height - PROGRESS_BAR_TOP_RIGHT.getHeight() - PROGRESS_BAR_BOTTOM_RIGHT.getHeight());
        scaledProgressBarTop = scaleImage(g, PROGRESS_BAR_TOP, scaledProgressBarTop,
                width - PROGRESS_BAR_TOP_LEFT.getWidth() - PROGRESS_BAR_TOP_RIGHT.getWidth(),
                PROGRESS_BAR_TOP.getHeight());
    }
}
