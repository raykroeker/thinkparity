/*
 * Created On: Jun 11, 2006 1:28:06 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.application.window;

import java.awt.*;

import javax.swing.border.AbstractBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;

/**
 * The thinkParity window border.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class WindowBorder extends AbstractBorder {
    
    /** The border insets. */
    private static final Insets BORDER_INSETS;

    static {
        BORDER_INSETS = new Insets(1, 1, 0, 0);    
    }

    /**
     * Create WindowBorder.
     * 
     */
    public WindowBorder() {
        super();
    }

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
        
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            // Top line
            g2.setColor(Colors.Browser.Window.BORDER_TOP);
            g2.drawLine(2, 0, width - 3, 0);
            
            // Gradient line on left
            final Paint gPaintLeft =
                new GradientPaint(0, 0, Colors.Browser.Window.BORDER_TOP_LEFT,
                                  0, height - 1, Colors.Browser.Window.BORDER_BOTTOM_LEFT);
            g2.setPaint(gPaintLeft);
            g2.fillRect(0, 2, 1, height - 3);
        }
        finally { g2.dispose(); }
    }
}
