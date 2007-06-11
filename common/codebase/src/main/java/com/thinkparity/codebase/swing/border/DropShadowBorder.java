/*
 * Created On: 26-Oct-06 3:00:02 PM
 */
package com.thinkparity.codebase.swing.border;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DropShadowBorder extends AbstractBorder {

    /** Dimensions of the shadow border. */
    private static final int SHADOW_TOP;
    private static final int SHADOW_LEFT;
    private static final int SHADOW_BOTTOM;
    private static final int SHADOW_RIGHT;

    static {
        SHADOW_TOP = 0;
        SHADOW_LEFT = 1;
        SHADOW_BOTTOM = 4;
        SHADOW_RIGHT = 4;
    }

    /** The swing <code>Robot</code>. */
    private final Robot robot;

    /** A screen capture <code>BufferedImage</code> of the bottom side of the menu.*/
    private BufferedImage screenCaptureBottom = null;

    /** A screen capture <code>BufferedImage</code> of the left side of the menu.*/
    private BufferedImage screenCaptureLeft = null;

    /** A screen capture <code>BufferedImage</code> of the right side of the menu.*/
    private BufferedImage screenCaptureRight = null;

    /** A screen capture <code>BufferedImage</code> of the top left of the menu.*/
    private BufferedImage screenCaptureTopLeft = null;

    /** A screen capture <code>BufferedImage</code> of the top right of the menu.*/
    private BufferedImage screenCaptureTopRight = null;

    /** The top border <code>Color</code>. */
    private final Color topColor;

    /**
     * Create DropShadowBorder.
     */
    public DropShadowBorder(final Color topColor) throws AWTException {
        super();
        this.robot = new Robot();
        this.topColor = topColor;
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */    
    public Insets getBorderInsets(final Component c) {
        return new Insets(1 + SHADOW_TOP, SHADOW_LEFT, SHADOW_BOTTOM, SHADOW_RIGHT);
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(final Component c, Insets insets) {
        insets.top = 1 + SHADOW_TOP;
        insets.left = SHADOW_LEFT;
        insets.bottom = SHADOW_BOTTOM;
        insets.right = SHADOW_RIGHT;

        return insets;
    }

    /**
     * @see javax.swing.border.AbstractBorder#getInteriorRectangle(java.awt.Component,
     *      int, int, int, int)
     * 
     */
    public Rectangle getInteriorRectangle(final Component c, final int x, final int y, final int width,
            final int height) {
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
    public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {               
        final Graphics2D g2 = (Graphics2D) g.create();
        try {      
            final int innerWidth = width - SHADOW_LEFT - SHADOW_RIGHT;

            // Draw a line at the top. 1 pixel on each edge are part of the rounded corner.
            g2.setColor(topColor);
            g2.drawLine(x + SHADOW_LEFT + 1, y + SHADOW_TOP,
                        x + SHADOW_LEFT + innerWidth - 2, y + SHADOW_TOP);

            // Perform screen captures on the sides of the component. Do this only
            // the first time, the second time it would capture shadow which is bad.
            if (null == screenCaptureRight) {
                final Point point = new Point(x + width - SHADOW_RIGHT, y);
                SwingUtilities.convertPointToScreen(point, c);
                screenCaptureRight = robot.createScreenCapture(
                        new Rectangle(point.x, point.y, SHADOW_RIGHT, height));
            }

            if (null == screenCaptureLeft) {
                final Point point = new Point(x, y);
                SwingUtilities.convertPointToScreen(point, c);
                screenCaptureLeft = robot.createScreenCapture(
                        new Rectangle(point.x, point.y, SHADOW_LEFT, height));
            }

            if (null == screenCaptureBottom) {
                final Point point = new Point(x + SHADOW_LEFT, y + height - SHADOW_BOTTOM);
                SwingUtilities.convertPointToScreen(point, c);
                screenCaptureBottom = robot.createScreenCapture(
                        new Rectangle(point.x, point.y, width - SHADOW_LEFT - SHADOW_RIGHT, SHADOW_BOTTOM));
            }

            if (null == screenCaptureTopLeft) {
                final Point point = new Point(x + SHADOW_LEFT, y);
                SwingUtilities.convertPointToScreen(point, c);
                screenCaptureTopLeft = robot.createScreenCapture(new Rectangle(point.x, point.y, 1, 1));
            }

            if (null == screenCaptureTopRight) {
                final Point point = new Point(x + width - SHADOW_RIGHT - 1, y);
                SwingUtilities.convertPointToScreen(point, c);
                screenCaptureTopRight = robot.createScreenCapture(new Rectangle(point.x, point.y, 1, 1));
            }

            // Draw the background images to the border.
            g2.drawImage(screenCaptureRight, null, x + width - SHADOW_RIGHT, y);
            g2.drawImage(screenCaptureLeft, null, x, y);
            g2.drawImage(screenCaptureBottom, null, x + SHADOW_LEFT, y + height - SHADOW_BOTTOM);
            g2.drawImage(screenCaptureTopLeft, null, x + SHADOW_LEFT, y);
            g2.drawImage(screenCaptureTopRight, null, x + width - SHADOW_RIGHT - 1, y);

            // Draw the shadow.
            g2.setColor(new Color(0, 0, 0, 255));

            g2.setComposite(makeComposite(0.6f));
            g2.draw(new Line2D.Double(x + width - 4, y + 7, x + width - 4, y + height - 5));   // right
            g2.draw(new Line2D.Double(x + 7, y + height - 4, x + width - 5, y + height - 4));  // bottom

            g2.setComposite(makeComposite(0.4f));
            g2.draw(new Line2D.Double(x + width - 3, y + 7, x + width - 3, y + height - 4));   // right
            g2.draw(new Line2D.Double(x + width - 4, y + 6, x + width - 4, y + 6));            // soften top right
            g2.draw(new Line2D.Double(x + 7, y + height - 3, x + width - 4, y + height - 3));  // bottom
            g2.draw(new Line2D.Double(x + 6, y + height - 4, x + 6, y + height - 4 ));         // soften bottom left

            g2.setComposite(makeComposite(0.2f));
            g2.draw(new Line2D.Double(x + width - 2, y + 7, x + width - 2, y + height - 3));   // right
            g2.draw(new Line2D.Double(x + width - 4, y + 5, x + width - 3, y + 6));            // soften top right
            g2.draw(new Line2D.Double(x + 7, y + height - 2, x + width - 3, y + height - 2));  // bottom
            g2.draw(new Line2D.Double(x + 5, y + height - 4, x + 6, y + height - 3 ));         // soften bottom left 

            g2.setComposite(makeComposite(0.1f));
            g2.draw(new Line2D.Double(x + width - 1, y + 7, x + width - 1, y + height - 2));   // right
            g2.draw(new Line2D.Double(x + width - 4, y + 4, x + width - 2, y + 6));            // soften top right
            g2.draw(new Line2D.Double(x + 7, y + height - 1, x + width - 2, y + height - 1));  // bottom
            g2.draw(new Line2D.Double(x + 4, y + height - 4, x + 6, y + height - 2 ));         // soften bottom left

            // Shadow on left. 2 pixels at the top are part of the rounded corner.
            g2.draw(new Line2D.Double(x, y + 2, x, y + height - 4));
            g2.draw(new Line2D.Double(x + 1, y + height - 4, x + 3, y + height - 4));

            // Shadow on top right. 2 pixels at the top are part of the rounded corner.
            g2.draw(new Line2D.Double(x + width - 4, y + 2, x + width - 4, y + 3));

            // Soften the shadow at the bottom right corner
            g2.setComposite(makeComposite(0.5f));
            g2.draw(new Line2D.Double(x + width - 4, y + height - 4, x + width - 4, y + height - 4));
            g2.setComposite(makeComposite(0.3f));
            g2.draw(new Line2D.Double(x + width - 3, y + height - 3, x + width - 3, y + height - 3));
            g2.setComposite(makeComposite(0.15f));
            g2.draw(new Line2D.Double(x + width - 2, y + height - 2, x + width - 2, y + height - 2));
        } finally {
            g2.dispose();
        }
    }

    /**
     * Force painting of the area underneath where the border shadows will be.
     * If this is not done then artifacts appear in the robot.createScreenCapture
     * because painting may not be complete yet.
     * This can't be called from paintBorder() above since paintAll() will cause
     * an infinite loop. Instead call it just before the popup will be visible.
     */
    public void paintUnderneathBorder(final Component c, final int x, final int y, final int width, final int height) {
        initializeScreenCaptures();
        Component deepestComponent = null;

        // Get the deepest component
        Component parent = c;
        while (null != parent) {
            deepestComponent = parent;
            parent = parent.getParent();
        }
        final Point point = SwingUtilities.convertPoint(c, x, y, deepestComponent);
        final Rectangle rect = new Rectangle();

        // If needed paint the dirty region under the right border.
        // Go one extra pixel wide to grab the rounded corner pixel top right.
        rect.x = point.x + width - SHADOW_RIGHT - 1;
        rect.y = point.y;
        rect.width = SHADOW_RIGHT + 1;
        rect.height = height;       
        paintUnderneathRectangle(deepestComponent, rect);

        // If needed paint the dirty region under the left border.
        // Go one extra pixel wide to grab the rounded corner pixel top left.
        rect.x = point.x;
        rect.y = point.y;
        rect.width = SHADOW_LEFT + 1;
        rect.height = height;       
        paintUnderneathRectangle(deepestComponent, rect);

        // If needed paint the dirty region under the bottom border.
        rect.x = point.x + SHADOW_LEFT;
        rect.y = point.y + height - SHADOW_BOTTOM;
        rect.width = width - SHADOW_LEFT - SHADOW_RIGHT;
        rect.height = SHADOW_BOTTOM;       
        paintUnderneathRectangle(deepestComponent, rect);
    }

    /**
     * Initialize screen captures so they will be regenerated later.
     */
    private void initializeScreenCaptures() {
        screenCaptureBottom = null;
        screenCaptureLeft = null;
        screenCaptureRight = null;
        screenCaptureTopLeft = null;
        screenCaptureTopRight = null;
    }

    private void paintUnderneathRectangle(final Component component, final Rectangle rect) {
        final int deepestComponentWidth = component.getWidth();
        final int deepestComponentHeight = component.getHeight();

        if ((rect.x + rect.width) > deepestComponentWidth) {
            rect.width = deepestComponentWidth - rect.x;
        }
        if ((rect.y + rect.height) > deepestComponentHeight) {
            rect.height = deepestComponentHeight - rect.y;
        }   

        if (!rect.isEmpty()) {
            final Graphics2D g2 = (Graphics2D)component.getGraphics();
            try { 
                g2.setClip(rect);
                if (component instanceof JComponent) {
                    JComponent jComponent = (JComponent) component;
                    boolean doubleBuffered = jComponent.isDoubleBuffered();
                    jComponent.setDoubleBuffered(false);
                    jComponent.paintAll(g2);
                    jComponent.setDoubleBuffered(doubleBuffered); 
                } else {
                    component.paintAll(g2);
                }
            } finally {
                g2.dispose();
            }
        }
    }

    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
}
