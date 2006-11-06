/**
 * Created On: 26-Oct-06 3:00:02 PM
 * $Id$
 */
package com.thinkparity.codebase.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

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
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
     *      java.awt.Insets)
     * 
     */
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = topThickness;
        insets.left = leftThickness;
        insets.bottom = bottomThickness;
        insets.right = rightThickness;
        
        return insets;
    }

    /**
     * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
     * 
     */
    public Insets getBorderInsets(Component c) {
        return new Insets(topThickness, leftThickness, bottomThickness, rightThickness);
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
    public void paintBorder(final Component c, final Graphics g, final int x,
            final int y, final int width, final int height) {
        final Graphics2D g2 = (Graphics2D) g.create();
        try {
            final Color oColor = g2.getColor();
            
            // Left
            g2.setColor(leftColors[0]);
            g2.drawLine(x, y, x, y + height - 1);
            for (Integer i = 1; i < leftThickness; i++) {
                g2.setColor(leftColors[i]);
                g2.drawLine(x + i, y, x + i, y + height - 1);
            }
            
            // Right
            g2.setColor(rightColors[0]);
            g2.drawLine(x + width - 1, y, x + width - 1, y + height - 1);
            for (Integer i = 1; i < rightThickness; i++) {
                g2.setColor(rightColors[i]);
                g2.drawLine(x + width - 1 - i, y, x + width - 1 - i, y + height - 1);
            }
            
            // Top
            g2.setColor(topColors[0]);
            g2.drawLine(x, y, x + width - 1, y);
            for (Integer i = 1; i < topThickness; i++) {
                g2.setColor(topColors[i]);
                g2.drawLine(x, y + i, x + width - 1, y + i);
            }
    
            // Bottom
            g2.setColor(bottomColors[0]);
            g2.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
            for (Integer i = 1; i < bottomThickness; i++) {
                g2.setColor(bottomColors[i]);
                g2.drawLine(x, y + height - 1 - i, x + width - 1, y + height - 1 - i);
            }
            
            g2.setColor(oColor);
        } finally {
            g2.dispose();
        }
    }
}
