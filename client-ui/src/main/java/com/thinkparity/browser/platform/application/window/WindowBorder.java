/*
 * Created On: Jun 11, 2006 1:28:06 PM
 * $Id$
 */
package com.thinkparity.browser.platform.application.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Stack;

import javax.swing.border.AbstractBorder;

import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * The thinkParity window border.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class WindowBorder extends AbstractBorder {

    /** The east border northern border. */
    public static final BufferedImage EAST_BORDER_NORTH;

    /** The east border southern border. */
    public static final BufferedImage EAST_BORDER_SOUTH;

    /** The west border northern border. */
    public static final BufferedImage WEST_BORDER_NORTH;

    /** The west border southern border. */
    public static final BufferedImage WEST_BORDER_SOUTH;

    /** The border insets. */
    private static final Insets BORDER_INSETS;

    /** A colour stack used when modifying the graphics object. */
    private static final Stack<Color> COLOUR_STACK;

    /** The border colours. */
    private static final Color[] COLOURS;

    /** The east and west border corner image dimensions. */
    private static final Dimension EAST_WEST_DIMENSIONS;

    /** The north border eastern image. */
    private static final BufferedImage NORTH_BORDER_EAST;

    /** The north border western image. */
    private static final BufferedImage NORTH_BORDER_WEST;

    /** The north and south border corner image dimensions. */
    private static final Dimension NORTH_SOUTH_DIMENSIONS;

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The south border eastern image. */
    private static final BufferedImage SOUTH_BORDER_EAST;

    /** The south border western image. */
    private static final BufferedImage SOUTH_BORDER_WEST;

    static {
        BORDER_INSETS = new Insets(4, 4, 4, 4);
        COLOURS = new Color[] {
                new Color(209, 223, 255, 255),
                new Color(193, 213, 255, 255),
                new Color(148, 165, 205, 255),
                new Color(92, 102, 127, 255)
        };
        COLOUR_STACK = new Stack<Color>();

        NORTH_BORDER_EAST = ImageIOUtil.read("NorthBorderEast.png");
        NORTH_BORDER_WEST = ImageIOUtil.read("NorthBorderWest.png");
        EAST_BORDER_NORTH = ImageIOUtil.read("EastBorderNorth.png");
        EAST_BORDER_SOUTH = ImageIOUtil.read("EastBorderSouth.png");
        SOUTH_BORDER_EAST = ImageIOUtil.read("SouthBorderEast.png");
        SOUTH_BORDER_WEST = ImageIOUtil.read("SouthBorderWest.png");
        WEST_BORDER_NORTH = ImageIOUtil.read("WestBorderNorth.png");
        WEST_BORDER_SOUTH = ImageIOUtil.read("WestBorderSouth.png");

        NORTH_SOUTH_DIMENSIONS = new Dimension(6, 4);
        EAST_WEST_DIMENSIONS = new Dimension(4, 2);
    }

    private static void popColour(final Graphics g) {
        g.setColor(COLOUR_STACK.pop());
    }

    private static void pushColour(final Graphics g) {
        COLOUR_STACK.push(g.getColor());
    }

    /** Create WindowBorder. */
    public WindowBorder() { super(); }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */
    public Insets getBorderInsets(Component c) {
        return (Insets) BORDER_INSETS.clone();
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = BORDER_INSETS.top;
        insets.left = BORDER_INSETS.left;
        insets.bottom = BORDER_INSETS.bottom;
        insets.right = BORDER_INSETS.right;
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
    public void paintBorder(final Component c, final Graphics g, final int x,
            final int y, final int width, final int height) {
        pushColour(g);

        final int eastXOffset = width - NORTH_SOUTH_DIMENSIONS.width;
        final int westXOffset = x;
        final int innerXOffset = x + NORTH_SOUTH_DIMENSIONS.width;
        final int yOffset = height - NORTH_SOUTH_DIMENSIONS.height;
        final int innerWidth = width - 2 * NORTH_SOUTH_DIMENSIONS.width;
        paintNorth(g, eastXOffset, westXOffset, innerXOffset, y, innerWidth);
        paintSouth(g, eastXOffset, westXOffset, innerXOffset, yOffset, innerWidth);

        final int xOffset = width - EAST_WEST_DIMENSIONS.width;
        final int northYOffset = NORTH_SOUTH_DIMENSIONS.height;
        final int southYOffset = height - (NORTH_SOUTH_DIMENSIONS.height + EAST_WEST_DIMENSIONS.height);
        final int innerYOffset = y + NORTH_SOUTH_DIMENSIONS.height + EAST_WEST_DIMENSIONS.height;
        final int innerHeight = height - (2 * NORTH_SOUTH_DIMENSIONS.height + 2 * EAST_WEST_DIMENSIONS.height);
        paintWest(g, x, northYOffset, southYOffset, innerYOffset, innerHeight);
        paintEast(g, xOffset, northYOffset, southYOffset, innerYOffset, innerHeight);

        popColour(g);
    }

    private void paintEast(final Graphics g, final int xOffset,
            final int northYOffset, final int southYOffset,
            final int innerYOffset, final int innerHeight) {
        g.drawImage(EAST_BORDER_NORTH, xOffset, northYOffset, null);
        g.drawImage(EAST_BORDER_SOUTH, xOffset, southYOffset, null);
        g.setColor(COLOURS[0]);
        g.drawRect(xOffset, innerYOffset, 1, innerHeight);
        g.setColor(COLOURS[1]);
        g.drawRect(xOffset + 1, innerYOffset, 1, innerHeight);
        g.setColor(COLOURS[2]);
        g.drawRect(xOffset + 2, innerYOffset, 1, innerHeight);
        g.setColor(COLOURS[3]);
        g.drawRect(xOffset + 3, innerYOffset, 1, innerHeight);
    }

    private void paintNorth(final Graphics g, final int eastXOffset,
            final int westXOffset, final int innerXOffset, final int y,
            final int innerWidth) {
        g.drawImage(NORTH_BORDER_WEST, westXOffset, y, null);
        g.drawImage(NORTH_BORDER_EAST, eastXOffset, y, null);
        g.setColor(COLOURS[0]);
        g.drawRect(innerXOffset, y, innerWidth, 1);
        g.setColor(COLOURS[1]);
        g.drawRect(innerXOffset, y + 1, innerWidth, 1);
        g.setColor(COLOURS[2]);
        g.drawRect(innerXOffset, y + 2, innerWidth, 1);
        g.setColor(COLOURS[3]);
        g.drawRect(innerXOffset, y + 3, innerWidth, 1);
    }

    private void paintSouth(final Graphics g, final int eastXOffset,
            final int westXOffset, final int innerXOffset, final int yOffset,
            final int innerWidth) {
        g.drawImage(SOUTH_BORDER_WEST, westXOffset, yOffset, null);
        g.drawImage(SOUTH_BORDER_EAST, eastXOffset, yOffset, null);
        g.setColor(COLOURS[0]);
        g.drawRect(innerXOffset, yOffset, innerWidth, 1);
        g.setColor(COLOURS[1]);
        g.drawRect(innerXOffset, yOffset + 1, innerWidth, 1);
        g.setColor(COLOURS[2]);
        g.drawRect(innerXOffset, yOffset + 2, innerWidth, 1);
        g.setColor(COLOURS[3]);
        g.drawRect(innerXOffset, yOffset + 3, innerWidth, 1);
    }

    private void paintWest(final Graphics g, final int x,
            final int northYOffset, final int southYOffset,
            final int innerYOffset, final int innerHeight) {
        g.drawImage(WEST_BORDER_NORTH, x, northYOffset, null);
        g.drawImage(WEST_BORDER_SOUTH, x, southYOffset, null);
        g.setColor(COLOURS[0]);
        g.drawRect(x, innerYOffset, 1, innerHeight);
        g.setColor(COLOURS[1]);
        g.drawRect(x + 1, innerYOffset, 1, innerHeight);
        g.setColor(COLOURS[2]);
        g.drawRect(x + 2, innerYOffset, 1, innerHeight);
        g.setColor(COLOURS[3]);
        g.drawRect(x + 3, innerYOffset, 1, innerHeight);
    }
}
