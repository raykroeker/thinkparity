/*
 * Created On: Jun 11, 2006 1:28:06 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.platform.application.window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Stack;

import javax.swing.border.AbstractBorder;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.Constants.Images;

/**
 * The thinkParity window border.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class WindowBorder2 extends AbstractBorder {

    /** The border insets. */
    private static final Insets BORDER_INSETS;

    /** A colour stack used when modifying the graphics object. */
    private static final Stack<Color> COLOR_STACK;
    
    /** The border colour. */
    private static final Color BORDER_COLOR;

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    static {
        BORDER_INSETS = new Insets(1, 1, 1, 1);
        BORDER_COLOR = Colors.Browser.Border.BORDER_COLOR;
        COLOR_STACK = new Stack<Color>();
    }

    private static void popColour(final Graphics g) {
        g.setColor(COLOR_STACK.pop());
    }

    private static void pushColour(final Graphics g) {
        COLOR_STACK.push(g.getColor());
    }

    /** Create WindowBorder. */
    public WindowBorder2() { super(); }

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

        g.setColor(BORDER_COLOR);
        
        g.drawLine(0, 0, width - 1, 0);                     // top line
        g.drawLine(0, 0, 0, height - 1);                    // left line
        g.drawLine(width - 1, 0, width - 1, height - 1);    // right line
        g.drawLine(0, height - 1, width - 1, height - 1);   // bottom line
        
        // These images put borders on rounded corners.
        if (!SwingUtil.isInMaximizedWindow(c)) {
            g.drawImage(Images.BrowserTitle.BROWSER_TOP_LEFT_OUTER,
                    0,
                    0,
                    Images.BrowserTitle.BROWSER_TOP_LEFT_OUTER.getWidth(),
                    Images.BrowserTitle.BROWSER_TOP_LEFT_OUTER.getHeight(), c);
            g.drawImage(Images.BrowserTitle.BROWSER_BOTTOM_LEFT_OUTER,
                    0,
                    height - Images.BrowserTitle.BROWSER_BOTTOM_LEFT_OUTER.getHeight(),
                    Images.BrowserTitle.BROWSER_BOTTOM_LEFT_OUTER.getWidth(),
                    Images.BrowserTitle.BROWSER_BOTTOM_LEFT_OUTER.getHeight(), c);
            g.drawImage(Images.BrowserTitle.BROWSER_TOP_RIGHT_OUTER,
                    width - Images.BrowserTitle.BROWSER_TOP_RIGHT_OUTER.getWidth(),
                    0,
                    Images.BrowserTitle.BROWSER_TOP_RIGHT_OUTER.getWidth(),
                    Images.BrowserTitle.BROWSER_TOP_RIGHT_OUTER.getHeight(), c);
            g.drawImage(Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_OUTER,
                    width - Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_OUTER.getWidth(),
                    height - Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_OUTER.getHeight(),
                    Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_OUTER.getWidth(),
                    Images.BrowserTitle.BROWSER_BOTTOM_RIGHT_OUTER.getHeight(), c);
        }

        popColour(g);
    }
}
