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

    /** The bottom border <code>Color[]</code>. */
    private final Color[] bottomColors;

    /** The bottom border thickness <code>Integer</code>. */
    private Integer bottomThickness = 1;

    /** The left border <code>Color[]</code>. */
    private final Color[] leftColors;

    /** The left border thickness <code>Integer</code>. */
    private Integer leftThickness = 1;
    
    /** The right border <code>Color[]</code>. */
    private final Color[] rightColors;

    /** The right border thickness <code>Integer</code>. */
    private Integer rightThickness = 1;

    /** The swing <code>Robot</code>. */
    private final Robot robot;

    /** A screen capture <code>BufferedImage</code> of the bottom side of the menu.*/
    private BufferedImage screenCaptureBottom = null;
    
    /** A screen capture <code>BufferedImage</code> of the right side of the menu.*/
    private BufferedImage screenCaptureRight = null;
    
    /** The top border <code>Color[]</code>. */
    private final Color[] topColors;

    /** The top border thickness <code>Integer</code>. */
    private Integer topThickness = 1;

    /**
     * Create DropShadowBorder.
     * 
     */
    public DropShadowBorder(final Color colors[], final Integer thickness)
            throws AWTException {
        super();
        this.topThickness = thickness;
        this.leftThickness = thickness;
        this.bottomThickness = thickness;
        this.rightThickness = thickness;
        this.robot = new Robot();
        this.topColors = new Color[thickness];
        this.leftColors = new Color[thickness];
        this.bottomColors = new Color[thickness];
        this.rightColors = new Color[thickness];
        for (int i=0; i<thickness; i++) {
            this.topColors[i] = colors[i];
            this.leftColors[i] = colors[i];
            this.bottomColors[i] = colors[i];
            this.rightColors[i] = colors[i];
        }
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */
    public Insets getBorderInsets(final Component c) {
        final int borderAdjust = 4;
        return new Insets(topThickness, leftThickness, bottomThickness + borderAdjust, rightThickness + borderAdjust);
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(final Component c, Insets insets) {
        final int borderAdjust = 4;
        insets.top = topThickness;
        insets.left = leftThickness;
        insets.bottom = bottomThickness + borderAdjust;
        insets.right = rightThickness + borderAdjust;
        
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
            final int borderAdjust = 4;
            final int innerWidth = width - borderAdjust;
            final int innerHeight = height - borderAdjust;
           
            // Left
            g2.setColor(leftColors[0]);
            g2.drawLine(x, y, x, y + innerHeight - 1);
            for (Integer i = 1; i < leftThickness; i++) {
                g2.setColor(leftColors[i]);
                g2.drawLine(x + i, y, x + i, y + innerHeight - 1);
            }
            
            // Right
            g2.setColor(rightColors[0]);
            g2.drawLine(x + innerWidth - 1, y, x + innerWidth - 1, y + innerHeight - 1);
            for (Integer i = 1; i < rightThickness; i++) {
                g2.setColor(rightColors[i]);
                g2.drawLine(x + innerWidth - 1 - i, y, x + innerWidth - 1 - i, y + innerHeight - 1);
            }
            
            // Top
            g2.setColor(topColors[0]);
            g2.drawLine(x, y, x + innerWidth - 1, y);
            for (Integer i = 1; i < topThickness; i++) {
                g2.setColor(topColors[i]);
                g2.drawLine(x + i, y + i, x + innerWidth - 1 - i, y + i);
            }
    
            // Bottom
            g2.setColor(bottomColors[0]);
            g2.drawLine(x, y + innerHeight - 1, x + innerWidth - 1, y + innerHeight - 1);
            for (Integer i = 1; i < bottomThickness; i++) {
                g2.setColor(bottomColors[i]);
                g2.drawLine(x + i, y + innerHeight - 1 - i, x + innerWidth - 1 - i, y + innerHeight - 1 - i);
            }
            
            // Grab the screen to the right of the menu. Make sure to do this only
            // the first time, the second time it would capture shadow which is bad.
            if (null == screenCaptureRight) {
                final Point point = new Point(x + width - 4, y);
                SwingUtilities.convertPointToScreen(point, c);
                screenCaptureRight = robot.createScreenCapture(
                        new Rectangle(point.x, point.y, 4, height));
            }
            
            // Grab the screen to the bottom of the menu.
            if (null == screenCaptureBottom) {
                final Point point = new Point(x, y + height - 4);
                SwingUtilities.convertPointToScreen(point, c);
                screenCaptureBottom = robot.createScreenCapture(
                        new Rectangle(point.x, point.y, width - 4, 4));
            }
            
            // Draw the background images to the border.
            g2.drawImage(screenCaptureRight, null, x + width - 4, y);
            g2.drawImage(screenCaptureBottom, null, x, y + height - 4);
            
            // Draw the shadow, four lines, from inner to outer.
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
        Component deepestComponent = null;
        
        // Get the deepest component
        Component parent = c;
        while (null != parent) {
            deepestComponent = parent;
            parent = parent.getParent();
        }
        final int deepestComponentWidth = deepestComponent.getWidth();
        final int deepestComponentHeight = deepestComponent.getHeight();
        final Point point = SwingUtilities.convertPoint(c, x, y, deepestComponent);
        final Rectangle rect = new Rectangle();
        
        // If needed paint the dirty region under the vertical border.
        rect.x = point.x + width - 4;
        rect.y = point.y;
        rect.width = 4;
        rect.height = height;
        if ((rect.x + rect.width) > deepestComponentWidth) {
            rect.width = deepestComponentWidth - rect.x;
        }
        if ((rect.y + rect.height) > deepestComponentHeight) {
            rect.height = deepestComponentHeight - rect.y;
        }        
        paintUnderneathRectangle(deepestComponent, rect);
        
        // If needed paint the dirty region under the horizontal border.
        rect.x = point.x;
        rect.y = point.y + height - 4;
        rect.width = width - 4;
        rect.height = 4;  
        if ((rect.x + rect.width) > deepestComponentWidth) {
            rect.width = deepestComponentWidth - rect.x;
        }
        if ((rect.y + rect.height) > deepestComponentHeight) {
            rect.height = deepestComponentHeight - rect.y;
        }        
        paintUnderneathRectangle(deepestComponent, rect);
    }
    
    private void paintUnderneathRectangle(final Component component, final Rectangle rect) {
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
