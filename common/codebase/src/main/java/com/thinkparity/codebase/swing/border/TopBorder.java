package com.thinkparity.codebase.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

/**
 * The default border for a display is a single pixel line at the top of the display.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TopBorder extends AbstractBorder {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;
    
    /**
     * The border colors.
     */
    private final Color[] colors;
    
    /**
     * The border thinkness.
     */
    private final Integer thickness;    
       
    /**
     * The border insets.
     */
    private final Insets insets;

	/**
	 * Create a black TopBorder.
	 * 
	 */
	public TopBorder() { this(Color.BLACK); }

	/**
	 * Create a TopBorder.
	 * 
	 * @param color
	 *            The border color.
	 */
	public TopBorder(final Color color) {
		super();
        this.colors = new Color[1];
		this.colors[0] = color;
        this.thickness = 1;
        this.insets = new Insets(1, 0, 0, 0);  // Offset top is 1
	}
    
    /**
     * Create a TopBorder.
     * 
     * @param color
     *            The border color.
     * @param thickness
     *            The border thinkness
     */
    public TopBorder(final Color color, final Integer thickness) {
        super();
        this.colors = new Color[thickness];
        for (int i=0; i<thickness; i++) {
            this.colors[i] = color;
        }
        this.thickness = thickness;
        this.insets = new Insets(1, 0, 0, 0);  // Offset top is 1
    }
    
    /**
     * Create a TopBorder.
     *
     * @param color
     *            The border color.
     * @param Insets
     *            The border insets.
     */
    public TopBorder(final Color color, Insets insets) {
        this(color);
        this.insets.top = insets.top;
        this.insets.left = insets.left;
        this.insets.bottom = insets.bottom;
        this.insets.right = insets.right;    
    }
    
    /**
     * Create a TopBorder.
     * 
     * @param color
     *            The border color.
     * @param thickness
     *            The border thinkness.
     * @param Insets
     *            The border insets.
     */
    public TopBorder(final Color color, final Integer thickness, Insets insets) {
        this(color, thickness);
        this.insets.top = insets.top;
        this.insets.left = insets.left;
        this.insets.bottom = insets.bottom;
        this.insets.right = insets.right;    
    }
    
    /**
     * Create a TopBorder.
     * 
     * @param colors
     *            The border colors, from top to bottom.
     * @param thickness
     *            The border thinkness.
     * @param Insets
     *            The border insets.
     */
    public TopBorder(final Color[] colors, final Integer thickness, Insets insets) {
        this(colors[0], thickness, insets);
        if (colors.length >= thickness) {
            for (int i=0; i<thickness; i++) {
                this.colors[i] = colors[i];
            }
        }
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
		g.setColor(colors[0]);
		g.drawLine(x + this.insets.left, y, x + width - 1 - this.insets.right, y);
        for (Integer i = 1; i < thickness; i++) {
            g.setColor(colors[i]);
            g.drawLine(x + this.insets.left, y + i, x + width - 1 - this.insets.right, y + i);
        }
		g.setColor(oColor);
	}
}
