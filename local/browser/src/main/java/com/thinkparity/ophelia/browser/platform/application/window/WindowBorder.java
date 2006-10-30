/*
 * Created On: Jun 11, 2006 1:28:06 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.application.window;

import java.awt.*;
import java.util.Stack;

import javax.swing.border.AbstractBorder;

import com.thinkparity.ophelia.browser.Constants.Colors;

/**
 * The thinkParity window border.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class WindowBorder extends AbstractBorder {
    
    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;
    
    /** The border insets. */
    private static final Insets BORDER_INSETS;

    /** A colour stack used when modifying the graphics object. */
    private static final Stack<Color> COLOR_STACK;

    /** The title height */
    private static final int TITLE_HEIGHT;

    static {
        BORDER_INSETS = new Insets(1, 3, 3, 3);    
        COLOR_STACK = new Stack<Color>();
        TITLE_HEIGHT = 25;
    }

    private static void popColour(final Graphics g) {
        g.setColor(COLOR_STACK.pop());
    }

    private static void pushColour(final Graphics g) {
        COLOR_STACK.push(g.getColor());
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
        
        final int titleY = TITLE_HEIGHT;
        
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            // Draw a gradient at top left and top right to match the WindowTitle
            final Paint gPaint =
                new GradientPaint(0, 1, Colors.Browser.Window.BG_GRAD_START,
                                  0, TITLE_HEIGHT + 1, Colors.Browser.Window.BG_GRAD_FINISH);
            g2.setPaint(gPaint);
            g2.fillRect(1, 1, 3, TITLE_HEIGHT - 1);
            g2.fillRect(width - 4, 1, 3, TITLE_HEIGHT - 1);
            
            // Draw a gradient for the inner line, left and right
            final Paint gPaintInner =
                new GradientPaint(2, titleY, Colors.Browser.Window.BORDER_COLOR_INNER_AT_TOP,
                                  2, height-3, Colors.Browser.Window.BORDER_COLOR_INNER_AT_BOTTOM);
            g2.setPaint(gPaintInner);
            g2.fillRect(2, titleY, 2, height - 3);
            g2.fillRect(width - 3, titleY, width - 3, height - 3);
            
            // Draw a gradient for the middle line, left and right 
            final Paint gPaintMid =
                new GradientPaint(2, titleY, Colors.Browser.Window.BORDER_COLOR_MID_AT_TOP,
                        2, height-3, Colors.Browser.Window.BORDER_COLOR_MID_AT_BOTTOM);
            g2.setPaint(gPaintMid);
            g2.fillRect(1, titleY, 1, height-2);
            g2.fillRect(width - 2, titleY, width - 2, height-2);
        }
        finally { g2.dispose(); }
        
        // Draw the outer border
        g.setColor(Colors.Browser.Window.BORDER_COLOR_OUTER);
        g.drawLine(0, 0, width - 1, 0);                     // top line
        g.drawLine(0, 0, 0, height - 1);                    // left line
        g.drawLine(width - 1, 0, width - 1, height - 1);    // right line
        g.drawLine(0, height - 1, width - 1, height - 1);   // bottom line
        
        // Draw the mid border at bottom
        g.setColor(Colors.Browser.Window.BORDER_COLOR_MID_AT_BOTTOM);
        g.drawLine(1, height - 2, width - 2, height - 2);      // bottom line
        
        // Draw the inner border at bottom
        g.setColor(Colors.Browser.Window.BORDER_COLOR_INNER_AT_BOTTOM);
        g.drawLine(2, height - 3, width - 3, height - 3);      // bottom line
       
               
        // These images put borders on rounded corners.
/*        g.drawImage(Images.BrowserTitle.DIALOG_TOP_LEFT_OUTER,
                0,
                0,
                Images.BrowserTitle.DIALOG_TOP_LEFT_OUTER.getWidth(),
                Images.BrowserTitle.DIALOG_TOP_LEFT_OUTER.getHeight(), c);
        g.drawImage(Images.BrowserTitle.DIALOG_BOTTOM_LEFT_OUTER,
                0,
                height - Images.BrowserTitle.DIALOG_BOTTOM_LEFT_OUTER.getHeight(),
                Images.BrowserTitle.DIALOG_BOTTOM_LEFT_OUTER.getWidth(),
                Images.BrowserTitle.DIALOG_BOTTOM_LEFT_OUTER.getHeight(), c);
        g.drawImage(Images.BrowserTitle.DIALOG_TOP_RIGHT_OUTER,
                width - Images.BrowserTitle.DIALOG_TOP_RIGHT_OUTER.getWidth(),
                0,
                Images.BrowserTitle.DIALOG_TOP_RIGHT_OUTER.getWidth(),
                Images.BrowserTitle.DIALOG_TOP_RIGHT_OUTER.getHeight(), c);
        g.drawImage(Images.BrowserTitle.DIALOG_BOTTOM_RIGHT_OUTER,
                width - Images.BrowserTitle.DIALOG_BOTTOM_RIGHT_OUTER.getWidth(),
                height - Images.BrowserTitle.DIALOG_BOTTOM_RIGHT_OUTER.getHeight(),
                Images.BrowserTitle.DIALOG_BOTTOM_RIGHT_OUTER.getWidth(),
                Images.BrowserTitle.DIALOG_BOTTOM_RIGHT_OUTER.getHeight(), c);*/
        
        popColour(g);
    }
}
