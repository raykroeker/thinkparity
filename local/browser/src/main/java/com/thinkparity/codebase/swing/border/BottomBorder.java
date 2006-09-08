package com.thinkparity.codebase.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

/**
 * A border that draws a single solid line at the bottom edge of a component.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BottomBorder extends AbstractBorder {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The border color.
	 * 
	 */
	private final Color color;
    
    /**
     * The border insets.
     */
    private final Insets insets;

	/**
	 * Create a black TopBorder.
	 * 
	 */
	public BottomBorder() { this(Color.BLACK); }

	/**
	 * Create a BottomBorder.
	 * 
	 * @param color
	 *            The border color.
	 */
	public BottomBorder(final Color color) {
		super();
		this.color = color;
        this.insets = new Insets(0, 0, 1, 0);  // Offset bottom is 1
	}
    
    /**
     * Create a BottomBorder.
     *
     * @param color
     *            The border color.
     * @param Insets
     *             The border insets.
     */
    public BottomBorder(final Color color, Insets insets) {
        this(color);
        this.insets.top = insets.top;
        this.insets.left = insets.left;
        this.insets.bottom = insets.bottom;
        this.insets.right = insets.right;       
    }

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
	 * 
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(insets.top, 0, insets.bottom, 0);
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
	 *      java.awt.Insets)
	 * 
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
        insets.top = this.insets.top;
        insets.left = 0;
        insets.bottom = this.insets.bottom;
        insets.right = 0;   
		return insets;
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
		g.setColor(color);
		g.drawLine(x + this.insets.left, y + height - 1, x + width - 1 - this.insets.right, y + height - 1);
		g.setColor(oColor);
	}
}
