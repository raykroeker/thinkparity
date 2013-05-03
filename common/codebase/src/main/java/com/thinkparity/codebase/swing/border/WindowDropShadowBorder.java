/**
 * Created On: 8-Jun-07 2:25:21 PM
 * $Id$
 */
package com.thinkparity.codebase.swing.border;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;

import com.thinkparity.codebase.swing.SwingUtil;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class WindowDropShadowBorder extends AbstractBorder {

    /** Width of the shadow border bottom. */
    private static int SHADOW_BOTTOM;

    /** Width of the shadow border left. */
    private static int SHADOW_LEFT;

    /** Bottom right shadow pattern. */
    private static int[][] SHADOW_PATTERN_BOTTOM_RIGHT = {
        {-1, 6, 6, 5, 4, 3, 2, 0},
        {6, 6, 5, 4, 4, 3, 2, 0},
        {6, 5, 5, 4, 3, 3, 1, 0},
        {5, 4, 4, 4, 3, 2, 1, 0},
        {4, 4, 3, 3, 3, 2, 1},
        {3, 3, 3, 2, 2, 1, 0},
        {2, 2, 1, 1, 1, 0},
        {0, 0, 0, 0}
    };

    /** Top left shadow pattern. */
    private static int[][] SHADOW_PATTERN_TOP_LEFT = {
        {-1, -1, 0, 0, 0},
        {-1, 0, 1, 1, 2},
        {0, 1, 2, 2, 3},
        {0, 1, 2, 3, 3},
        {0, 2, 3, 3}
    };

    /** Top right shadow pattern. (Inverse is bottom left.) */
    private static int[][] SHADOW_PATTERN_TOP_RIGHT = {
        {1, 0, 0},
        {2, 1, 1, 0, 0},
        {3, 2, 2, 2, 1, 0},
        {3, 3, 3, 3, 2, 1, 0},
        {-1, 4, 4, 3, 3, 2, 1},
        {-1, -1, 4, 4, 3, 2, 1, 0},
        {-1, -1, 5, 4, 3, 2, 1, 1},
        {-1, -1, 5, 4, 3, 2, 2, 1},
        {-1, -1, 5, 4, 3, 3, 2, 1},
        {-1, -1, 5, 4, 4, 3, 2, 1},
        {-1, -1, 5, 5, 4, 3, 2, 1}
    };

    /** Width of the shadow border right. */
    private static int SHADOW_RIGHT;

    /** Width of the shadow border top. */
    private static int SHADOW_TOP;

    static {
        SHADOW_BOTTOM = 6;
        SHADOW_LEFT = 3;
        SHADOW_RIGHT = 6;
        SHADOW_TOP = 3;
    }

    /** The inner border bottom colour. */
    private final Color bottomColor;

    /** The bottom colour of the gradient border on the left. */
    private final Color bottomLeftColor;

    /** The bottom colour of the gradient border on the right. */
    private final Color bottomRightColor;

    /** The swing <code>Robot</code>. */
    private final Robot robot;

    /** A screen capture <code>BufferedImage</code> of the bottom.*/
    private BufferedImage screenCaptureBottom = null;

    /** A screen capture <code>BufferedImage</code> of the bottom left.*/
    private BufferedImage screenCaptureBottomLeft = null;

    /** A screen capture <code>BufferedImage</code> of the bottom right.*/
    private BufferedImage screenCaptureBottomRight = null;

    /** A screen capture <code>BufferedImage</code> of the left.*/
    private BufferedImage screenCaptureLeft = null;

    /** A screen capture <code>BufferedImage</code> of the right.*/
    private BufferedImage screenCaptureRight = null;

    /** A screen capture <code>BufferedImage</code> of the top.*/
    private BufferedImage screenCaptureTop = null;

    /** A screen capture <code>BufferedImage</code> of the top left.*/
    private BufferedImage screenCaptureTopLeft = null;

    /** A screen capture <code>BufferedImage</code> of the top right.*/
    private BufferedImage screenCaptureTopRight = null;

    /** The inner border top colour. */
    private final Color topColor;

    /** The top colour of the gradient border on the left. */
    private final Color topLeftColor;

    /** The top colour of the gradient border on the right. */
    private Color topRightColor;

    /** The <code>Window</code> with the border. */
    private final java.awt.Window window;

    /**
     * Create WindowDropShadowBorder.
     * 
     * @param window
     *            The <code>Window</code>.
     */
    public WindowDropShadowBorder(final java.awt.Window window,
            final Color topColor, final Color bottomColor,
            final Color topLeftColor, final Color bottomLeftColor,
            final Color topRightColor, final Color bottomRightColor) throws AWTException {
        super();
        this.window = window;
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        this.topLeftColor = topLeftColor;
        this.bottomLeftColor = bottomLeftColor;
        this.topRightColor = topRightColor;
        this.bottomRightColor = bottomRightColor;
        this.robot = new Robot();
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */
    public Insets getBorderInsets(final Component c) {
        return new Insets(SHADOW_TOP + 1, SHADOW_LEFT + 1, SHADOW_BOTTOM + 1, SHADOW_RIGHT + 1);
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(final Component c, Insets insets) {
        insets.top = SHADOW_TOP + 1;
        insets.left = SHADOW_LEFT + 1;
        insets.bottom = SHADOW_BOTTOM + 1;
        insets.right = SHADOW_RIGHT + 1;

        return insets;
    }

    /**
     * @see javax.swing.border.AbstractBorder#getInteriorRectangle(java.awt.Component,
     *      int, int, int, int)
     * 
     */
    public Rectangle getInteriorRectangle(final Component c, final int x,
            final int y, final int width, final int height) {
        return super.getInteriorRectangle(c, x, y, width, height);
    }

    /**
     * Initialize the border.
     * This should be called just before the window becomes visible.
     */
    public void initialize() {
        initializeScreenCaptures();
        paintUnderneathBorder();
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
    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {               
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            // perform screen captures on the sides of the component where the border will be.
            captureScreenImages(c, x, y, width, height);

            // draw solid border lines
            drawSolidBorderLines(g2, x, y, width, height);

            // draw the background images to the border.
            g2.drawImage(screenCaptureLeft, null, x, y);
            g2.drawImage(screenCaptureRight, null, x + width - SHADOW_RIGHT, y);
            g2.drawImage(screenCaptureTop, null, x + SHADOW_LEFT, y);
            g2.drawImage(screenCaptureBottom, null, x + SHADOW_LEFT, y + height - SHADOW_BOTTOM);
            g2.drawImage(screenCaptureTopLeft, null, x + SHADOW_LEFT, y + SHADOW_TOP);
            g2.drawImage(screenCaptureTopRight, null, x + width - SHADOW_RIGHT - 2, y + SHADOW_TOP);
            g2.drawImage(screenCaptureBottomLeft, null, x + SHADOW_LEFT, y + height - SHADOW_BOTTOM - 2);
            g2.drawImage(screenCaptureBottomRight, null, x + width - SHADOW_RIGHT - 2, y + height - SHADOW_BOTTOM - 2);

            // draw shadows
            drawShadows(g2, x, y, width, height);

        } finally {
            g2.dispose();
        }
    }

    /**
     * Capture a screen image.
     * 
     * @param c
     *            A <code>Component</code>.
     * @param x
     *            The x offset <code>int</code>.
     * @param y
     *            The y offset <code>int</code>.
     * @param width
     *            The width <code>int</code>.
     * @param height
     *            The height <code>int</code>.
     * @return A <code>BufferedImage</code>.
     */
    private BufferedImage captureScreenImage(final Component c, final int x, final int y, final int width, final int height) {
        final Point point = new Point(x, y);
        SwingUtilities.convertPointToScreen(point, c);
        return robot.createScreenCapture(new Rectangle(point.x, point.y, width, height));
    }

    /**
     * Capture the screen images. This is done only the first time.
     * 
     * @param c
     *            A <code>Component</code>.
     * @param x
     *            The x offset <code>int</code>.
     * @param y
     *            The y offset <code>int</code>.
     * @param width
     *            The width <code>int</code>.
     * @param height
     *            The height <code>int</code>.
     */
    private void captureScreenImages(final Component c, final int x, final int y, final int width, final int height) {
        if (null == screenCaptureBottom) {
            screenCaptureBottom = captureScreenImage(c, x + SHADOW_LEFT,
                    y + height - SHADOW_BOTTOM, width - SHADOW_LEFT - SHADOW_RIGHT, SHADOW_BOTTOM);
        }
        if (null == screenCaptureBottomLeft) {
            screenCaptureBottomLeft = captureScreenImage(c, x + SHADOW_LEFT, y + height - SHADOW_BOTTOM - 2, 2, 2);
        }
        if (null == screenCaptureBottomRight) {
            screenCaptureBottomRight = captureScreenImage(c, x + width - SHADOW_RIGHT - 2, y + height - SHADOW_BOTTOM - 2, 2, 2);
        }
        if (null == screenCaptureLeft) {
            screenCaptureLeft = captureScreenImage(c, x, y, SHADOW_LEFT, height);
        }
        if (null == screenCaptureRight) {
            screenCaptureRight = captureScreenImage(c, x + width - SHADOW_RIGHT, y, SHADOW_RIGHT, height);
        }
        if (null == screenCaptureTop) {
            screenCaptureTop = captureScreenImage(c, x + SHADOW_LEFT, y,
                    width - SHADOW_LEFT - SHADOW_RIGHT, SHADOW_TOP);
        }
        if (null == screenCaptureTopLeft) {
            screenCaptureTopLeft = captureScreenImage(c, x + SHADOW_LEFT, y + SHADOW_TOP, 2, 2);
        }
        if (null == screenCaptureTopRight) {
            screenCaptureTopRight = captureScreenImage(c, x + width - SHADOW_RIGHT - 2, y + SHADOW_TOP, 2, 2);
        }
    }

    /**
     * Draw a corner shadow.
     * 
     * Each entry in the 2D pattern array contains the index
     * to the appropriate composite for the corresponding pixel,
     * or a negative number to draw nothing.
     * 
     * @param g2
     *            A <code>Graphics2D</code>.
     * @param x
     *            The x offset <code>int</code>.
     * @param y
     *            The y offset <code>int</code>.
     * @param pattern
     *            A 2D pattern array <code>int[][]</code>.
     * @param inversePattern
     *            A <code>Boolean</code>, true to inverse the pattern.
     * @param composites
     *            An array of <code>AlphaComposite</code>.
     */
    private void drawCorner(final Graphics2D g2, final int x, final int y,
            int[][] pattern, final Boolean inversePattern,
            final AlphaComposite[] composites) {
        final Line2D.Double line = new Line2D.Double();
        for (int row = 0; row < pattern.length; row++) {
            for (int col = 0; col < pattern[row].length; col++) {
                int value = pattern[row][col];
                if (value >= 0) {
                    g2.setComposite(composites[value]);
                    if (inversePattern) {
                        line.setLine(x+row, y+col, x+row, y+col);
                    } else {
                        line.setLine(x+col, y+row, x+col, y+row);
                    }
                    g2.draw(line);  
                }
            }
        }
    }

    /**
     * Draw semi-transparent shadows.
     * 
     * @param g2
     *            A <code>Graphics2D</code>.
     * @param x
     *            The x offset <code>int</code>.
     * @param y
     *            The y offset <code>int</code>.
     * @param width
     *            The width <code>int</code>.
     * @param height
     *            The height <code>int</code>.
     */
    private void drawShadows(final Graphics2D g2, final int x, final int y, final int width, final int height) {
        // set color to black
        g2.setColor(new Color(0, 0, 0, 255));

        // make composites
        final AlphaComposite[] composites = new AlphaComposite[] {
                makeComposite(0.025f),
                makeComposite(0.05f), makeComposite(0.1f),
                makeComposite(0.15f), makeComposite(0.2f),
                makeComposite(0.25f), makeComposite(0.3f)
        };

        // draw top left corner
        drawCorner(g2, x, y, SHADOW_PATTERN_TOP_LEFT, Boolean.FALSE, composites);

        // draw top right corner
        drawCorner(g2, x + width - SHADOW_RIGHT - 2, y,
                SHADOW_PATTERN_TOP_RIGHT, Boolean.FALSE, composites);

        // draw bottom left corner
        drawCorner(g2, x, y + height - SHADOW_BOTTOM - 2,
                SHADOW_PATTERN_TOP_RIGHT, Boolean.TRUE, composites);

        // draw bottom right corner
        drawCorner(g2, x + width - SHADOW_RIGHT - 2, y + height - SHADOW_BOTTOM -2,
                SHADOW_PATTERN_BOTTOM_RIGHT, Boolean.FALSE, composites);

        // draw left
        for (int i = 0; i < 3; i++) {
            g2.setComposite(composites[1+i]);
            g2.draw(new Line2D.Double(x + i, y + SHADOW_TOP + 2,
                    x + i, y + height - SHADOW_BOTTOM - 3));
        }

        // draw top
        for (int i = 0; i < 3; i++) {
            g2.setComposite(composites[1+i]);
            g2.draw(new Line2D.Double(x + SHADOW_LEFT + 2, y + i,
                    x + width - SHADOW_RIGHT - 3, y + i));
        }

        // draw right
        for (int i = 0; i < 6; i++) {
            g2.setComposite(composites[6-i]);
            g2.draw(new Line2D.Double(x + width - SHADOW_RIGHT + i, y + SHADOW_TOP + 8,
                    x + width - SHADOW_RIGHT + i, y + height - SHADOW_BOTTOM - 3));
        }

        // draw bottom
        for (int i = 0; i < 6; i++) {
            g2.setComposite(composites[6-i]);
            g2.draw(new Line2D.Double(x + SHADOW_LEFT + 8, y + height - SHADOW_BOTTOM + i,
                    x + width - SHADOW_RIGHT - 3, y + height - SHADOW_BOTTOM + i));
        }
    }

    /**
     * Draw solid border lines.
     * Two pixels on either side of each line are not drawn, to
     * account for rounded corners.
     * 
     * @param g2
     *            A <code>Graphics2D</code>.
     * @param x
     *            The x offset <code>int</code>.
     * @param y
     *            The y offset <code>int</code>.
     * @param width
     *            The width <code>int</code>.
     * @param height
     *            The height <code>int</code>.
     */
    private void drawSolidBorderLines(final Graphics2D g2, final int x, final int y, final int width, final int height) {
        // top line
        g2.setColor(topColor);
        g2.drawLine(x + SHADOW_LEFT + 2, y + SHADOW_TOP, x + width - SHADOW_RIGHT - 3, y + SHADOW_TOP);

        // bottom line
        g2.setColor(bottomColor);
        g2.drawLine(x + SHADOW_LEFT + 2, y + height - SHADOW_BOTTOM - 1,
                    x + width - SHADOW_RIGHT - 3, y + height - SHADOW_BOTTOM - 1);

        // left line
        final Paint gPaintLeft =
            new GradientPaint(x + SHADOW_LEFT, y + SHADOW_TOP + 2, topLeftColor,
                              x + SHADOW_LEFT, y + height - SHADOW_BOTTOM - 3, bottomLeftColor);
        g2.setPaint(gPaintLeft);
        g2.fillRect(x + SHADOW_LEFT, y + SHADOW_TOP + 2, 1, y + height - SHADOW_BOTTOM - SHADOW_TOP - 4);

        // right line
        final Paint gPaintRight =
            new GradientPaint(x + width - SHADOW_RIGHT - 1, y + SHADOW_TOP + 2, topRightColor,
                              x + width - SHADOW_RIGHT - 1, y + height - SHADOW_BOTTOM - 3, bottomRightColor);
        g2.setPaint(gPaintRight);
        g2.fillRect(x + width - SHADOW_RIGHT - 1, y + SHADOW_TOP + 2, 1, y + height - SHADOW_BOTTOM - SHADOW_TOP - 4);
    }

    /**
     * Initialize screen captures so they will be regenerated later.
     */
    private void initializeScreenCaptures() {
        screenCaptureBottom = null;
        screenCaptureBottomLeft = null;
        screenCaptureBottomRight = null;
        screenCaptureLeft = null;
        screenCaptureRight = null;
        screenCaptureTop = null;
        screenCaptureTopLeft = null;
        screenCaptureTopRight = null;
    }

    /**
     * Make an alpha composite.
     * 
     * @param alpha
     *            The alpha value <code>float</code>.
     * @return An <code>AlphaComposite</code>.
     */
    private AlphaComposite makeComposite(final float alpha) {
        return(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    /**
     * Force painting on the owner window underneath where the border shadows will be.
     * 
     * Note that this can't be called from paintBorder() since paintAll() will cause
     * an infinite loop. Instead it is called just before the window will be visible.
     */
    private void paintUnderneathBorder() {
        final Window owner = window.getOwner();
        if (null != owner) {
            // determine the location and dimensions of the window relative to its owner
            final Point windowLocation = window.getLocation();
            final Point ownerLocation = owner.getLocation();
            final Point location = new Point(windowLocation.x - ownerLocation.x,
                    windowLocation.y - ownerLocation.y);
            final Dimension dimension = window.getPreferredSize();

            // ensure dimensions are within the owner boundaries
            if (location.x < 0) {
                dimension.width += location.x;
                location.x = 0;
            }
            if (location.y < 0) {
                dimension.height += location.y;
                location.y = 0;
            }
            if ((location.x + dimension.width) > owner.getWidth()) {
                dimension.width = owner.getWidth() - location.x;
            }
            if ((location.y + dimension.height) > owner.getHeight()) {
                dimension.height = owner.getHeight() - location.y;
            }

            // paint under border regions
            paintWindowRectangle(owner, location.x, location.y, dimension.width, SHADOW_TOP);
            paintWindowRectangle(owner, location.x, location.y + dimension.height - SHADOW_BOTTOM,
                    dimension.width, SHADOW_BOTTOM);
            paintWindowRectangle(owner, location.x, location.y + SHADOW_TOP,
                    SHADOW_LEFT, dimension.height - SHADOW_TOP - SHADOW_BOTTOM);
            paintWindowRectangle(owner, location.x + dimension.width - SHADOW_RIGHT, location.y + SHADOW_TOP,
                    SHADOW_RIGHT, dimension.height - SHADOW_TOP - SHADOW_BOTTOM);

            // paint the 2x2 areas in the rounded corners
            paintWindowRectangle(owner, location.x + SHADOW_LEFT,
                    location.y + SHADOW_TOP, 2, 2);
            paintWindowRectangle(owner, location.x + dimension.width - SHADOW_RIGHT - 2,
                    location.y + SHADOW_TOP, 2, 2);
            paintWindowRectangle(owner, location.x + SHADOW_LEFT,
                    location.y + dimension.height - SHADOW_BOTTOM - 2, 2, 2);
            paintWindowRectangle(owner, location.x + dimension.width - SHADOW_RIGHT - 2,
                    location.y + dimension.height - SHADOW_BOTTOM - 2, 2, 2);
        }
    }

    /**
     * Paint a rectangle on a window.
     * 
     * @param window
     *            The <code>Window</code>.
     * @param x
     *            The x offset <code>int</code>.
     * @param y
     *            The y offset <code>int</code>.
     * @param width
     *            The width <code>int</code>.
     * @param height
     *            The height <code>int</code>.
     */
    private void paintWindowRectangle(final Window window, final int x, final int y, final int width, final int height) {
        final Rectangle rect = new Rectangle(x, y, width, height);
        if (!rect.isEmpty()) {
            final Graphics2D g2 = (Graphics2D)window.getGraphics();
            try { 
                g2.setClip(rect);
                SwingUtil.ensureDispatchThread(new Runnable() {
                    public void run() {
                        window.paintAll(g2);
                    }
                });
            } finally {
                g2.dispose();
            }
        }
    }
}
