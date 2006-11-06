/**
 * Created On: 26-Oct-06 3:00:02 PM
 * $Id$
 */
package com.thinkparity.codebase.swing.border;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;

import com.thinkparity.ophelia.browser.BrowserException;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class MultiColourLineBorder extends AbstractBorder {
    
    /**
     * @see java.io.Serializable
     * 
     */
    private static final long serialVersionUID = 1;
    
    /** An apache logger error statement. */
    private static final String ROBOT_ERROR = "[ROBOT SCREEN CAPTURE ERROR]";

    /**
     * The border colors (top, left, bottom, right).
     */
    private final Color[] topColors;
    private final Color[] leftColors;
    private final Color[] bottomColors;
    private final Color[] rightColors;
    
    /*
     * The border thickness (top, left, bottom, right).
     */
    private Integer topThickness = 1;
    private Integer leftThickness = 1;
    private Integer bottomThickness = 1;
    private Integer rightThickness = 1;
    
    /** Shadow. */
    private Boolean shadow = Boolean.FALSE;
    
    /** Image captures. */
    private BufferedImage screenCaptureRight = null;
    private BufferedImage screenCaptureBottom = null;

    /**
     * Create a MultiColourLineBorder.
     * 
     */
    public MultiColourLineBorder(final Color color) {
        super();
        this.topColors = new Color[1];
        this.leftColors = new Color[1];
        this.bottomColors = new Color[1];
        this.rightColors = new Color[1];
        this.topColors[0] = color;
        this.leftColors[0] = color;
        this.bottomColors[0] = color;
        this.rightColors[0] = color;
    }
    
    /**
     * Create a MultiColourLineBorder.
     * 
     */
    public MultiColourLineBorder(final Color color, final Boolean shadow) {
        this(color);
        this.shadow = shadow;
    }
    
    /**
     * Create a MultiColourLineBorder.
     * 
     */
    public MultiColourLineBorder(final Color topColor, final Color leftColor, final Color bottomColor, final Color rightColor) {
        super();
        this.topColors = new Color[1];
        this.leftColors = new Color[1];
        this.bottomColors = new Color[1];
        this.rightColors = new Color[1];
        this.topColors[0] = topColor;
        this.leftColors[0] = leftColor;
        this.bottomColors[0] = bottomColor;
        this.rightColors[0] = rightColor;
    }
    
    /**
     * Create a MultiColourLineBorder.
     * 
     */
    public MultiColourLineBorder(final Color topColor, final Color leftColor,
            final Color bottomColor, final Color rightColor, final Boolean shadow) {
        this(topColor, leftColor, bottomColor, rightColor);
        this.shadow = shadow;
    }
    
    /**
     * Create a MultiColourLineBorder.
     * 
     */
    public MultiColourLineBorder(final Color colors[], final Integer thickness) {
        super();
        this.topThickness = thickness;
        this.leftThickness = thickness;
        this.bottomThickness = thickness;
        this.rightThickness = thickness;
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
     * Create a MultiColourLineBorder.
     * 
     */
    public MultiColourLineBorder(final Color colors[], final Integer thickness, final Boolean shadow) {
        this(colors, thickness);
        this.shadow = shadow;
    }
    
    /**
     * Create a MultiColourLineBorder.
     * 
     */
    public MultiColourLineBorder(final Color topColors[], final Integer topThickness,
            final Color[] leftColors, final Integer leftThickness,
            final Color[] bottomColors, final Integer bottomThickness,
            final Color[] rightColors, final Integer rightThickness) {
        super();
        this.topThickness = topThickness;
        this.leftThickness = leftThickness;
        this.bottomThickness = bottomThickness;
        this.rightThickness = rightThickness;
        
        this.topColors = new Color[topThickness];
        for (int i=0; i<topThickness; i++) {
            this.topColors[i] = topColors[i];
        }
        this.leftColors = new Color[leftThickness];
        for (int i=0; i<leftThickness; i++) {
            this.leftColors[i] = leftColors[i];
        }
        this.bottomColors = new Color[bottomThickness];
        for (int i=0; i<bottomThickness; i++) {
            this.bottomColors[i] = bottomColors[i];
        }
        this.rightColors = new Color[rightThickness];
        for (int i=0; i<rightThickness; i++) {
            this.rightColors[i] = rightColors[i];
        }
    }
    
    /**
     * Create a MultiColourLineBorder.
     * 
     */
    public MultiColourLineBorder(final Color topColors[], final Integer topThickness,
            final Color[] leftColors, final Integer leftThickness,
            final Color[] bottomColors, final Integer bottomThickness,
            final Color[] rightColors, final Integer rightThickness, final Boolean shadow) {
        this(topColors, topThickness, leftColors, leftThickness, bottomColors, bottomThickness, rightColors, rightThickness);
        this.shadow = shadow;
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        final int borderAdjust = shadow ? 4 : 0;
        insets.top = topThickness;
        insets.left = leftThickness;
        insets.bottom = bottomThickness + borderAdjust;
        insets.right = rightThickness + borderAdjust;
        
        return insets;
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */
    public Insets getBorderInsets(Component c) {
        final int borderAdjust = shadow ? 4 : 0;
        return new Insets(topThickness, leftThickness, bottomThickness + borderAdjust, rightThickness + borderAdjust);
    }

    /**
     * @see javax.swing.border.AbstractBorder#getInteriorRectangle(java.awt.Component,
     *      int, int, int, int)
     * 
     */
    public Rectangle getInteriorRectangle(Component c, int x, int y, int width,
            int height) {
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
        final Color oColor = g.getColor();
        
        final int borderAdjust = shadow ? 4 : 0;
        final int innerWidth = width - borderAdjust;
        final int innerHeight = height - borderAdjust;
        
        // Left
        g.setColor(leftColors[0]);
        g.drawLine(x, y, x, y + innerHeight - 1);
        for (Integer i = 1; i < leftThickness; i++) {
            g.setColor(leftColors[i]);
            g.drawLine(x + i, y, x + i, y + innerHeight - 1);
        }
        
        // Right
        g.setColor(rightColors[0]);
        g.drawLine(x + innerWidth - 1, y, x + innerWidth - 1, y + innerHeight - 1);
        for (Integer i = 1; i < rightThickness; i++) {
            g.setColor(rightColors[i]);
            g.drawLine(x + innerWidth - 1 - i, y, x + innerWidth - 1 - i, y + innerHeight - 1);
        }
        
        // Top
        g.setColor(topColors[0]);
        g.drawLine(x, y, x + innerWidth - 1, y);
        for (Integer i = 1; i < topThickness; i++) {
            g.setColor(topColors[i]);
            g.drawLine(x + i, y + i, x + innerWidth - 1 - i, y + i);
        }

        // Bottom
        g.setColor(bottomColors[0]);
        g.drawLine(x, y + innerHeight - 1, x + innerWidth - 1, y + innerHeight - 1);
        for (Integer i = 1; i < bottomThickness; i++) {
            g.setColor(bottomColors[i]);
            g.drawLine(x + i, y + innerHeight - 1 - i, x + innerWidth - 1 - i, y + innerHeight - 1 - i);
        }
        
        // Shadow
        if (shadow) {
            final Graphics2D g2 = (Graphics2D) g.create();
            try {
                // Grab the screen to the right of the menu. Make sure to do this only
                // the first time, the second time it would capture shadow which is bad.
                if (null == screenCaptureRight) {
                    final Point point = new Point(x + width - 4, y);
                    SwingUtilities.convertPointToScreen(point, c);
                    screenCaptureRight = new Robot().createScreenCapture(
                            new Rectangle(point.x, point.y, 4, height));
                }
                
                // Grab the screen to the bottom of the menu.
                if (null == screenCaptureBottom) {
                    final Point point = new Point(x, y + height - 4);
                    SwingUtilities.convertPointToScreen(point, c);
                    screenCaptureBottom = new Robot().createScreenCapture(
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
            }
            catch (final Throwable t) {
                throw new BrowserException(ROBOT_ERROR, t);  
            }
            finally { g2.dispose(); }
        }
                
        g.setColor(oColor);
    }
    
    private AlphaComposite makeComposite(float alpha) {
        int type = AlphaComposite.SRC_OVER;
        return(AlphaComposite.getInstance(type, alpha));
    }
}
