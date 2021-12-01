/*
 * Apr 10, 2006
 */
package com.thinkparity.codebase.swing.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.border.AbstractBorder;

/**
 * Uses a set of 4 images to draw a border.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class ImageBorder extends AbstractBorder {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The bottom border image. */
    private final BufferedImage bottom;

    /** The bottom border height (inset) . */
    private final Integer bottomHeight;

    /** The left border image. */
    private final BufferedImage left;

    /** The left border width (inset). */
    private final Integer leftWidth;

    /** The right border image. */
    private final BufferedImage right;

    /** The right border width (inset). */
    private final Integer rightWidth;

    /** The top border image. */
    private final BufferedImage top;

    /** The top border height (inset). */

    private final Integer topHeight;

    /**
     * Create an ImageBorder.
     * 
     * @param top
     *            The top border image.
     * @param left
     *            The top border image.
     * @param bottom
     *            The top border image.
     * @param right
     *            The top border image.
     */
    public ImageBorder(final BufferedImage top, final BufferedImage left,
            final BufferedImage bottom, final BufferedImage right) {
        super();
        this.top = top;
        this.topHeight = top.getHeight();
        this.left = left;
        this.leftWidth = left.getWidth();
        this.bottom = bottom;
        this.bottomHeight = bottom.getHeight();
        this.right = right;
        this.rightWidth = right.getWidth();
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */
    public Insets getBorderInsets(Component c) {
        return new Insets(topHeight, leftWidth, bottomHeight, rightWidth);
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = topHeight;
        insets.left = leftWidth;
        insets.bottom = bottomHeight;
        insets.right = rightWidth;
        return insets;
    }

    /**
     * @see javax.swing.border.AbstractBorder#getInteriorRectangle(java.awt.Component,
     *      int, int, int, int)
     * 
     */
    public Rectangle getInteriorRectangle(Component c, int x, int y, int width, int height) {
        return super.getInteriorRectangle(c, x, y, width, height);
    }

    /**
     * @see javax.swing.border.AbstractBorder#isBorderOpaque()
     * 
     */
    public boolean isBorderOpaque() { return true; }

    /**
     * @see javax.swing.border.AbstractBorder#paintBorder(java.awt.Component,
     *      java.awt.Graphics, int, int, int, int)
     * 
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawImage(top, 0, 0, null);
        g.drawImage(left, 0, topHeight, null);
        g.drawImage(bottom, 0, c.getHeight() - bottomHeight, null);
        g.drawImage(right, c.getWidth() - rightWidth, topHeight, null);
    }
}
