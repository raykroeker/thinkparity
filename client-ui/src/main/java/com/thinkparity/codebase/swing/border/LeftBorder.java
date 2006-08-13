package com.thinkparity.codebase.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

/**
 * A border that draws a single solid line at the left edge of a component.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class LeftBorder extends AbstractBorder {

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
	 * Create a black TopBorder.
	 * 
	 */
	public LeftBorder() { this(Color.BLACK); }

	/**
	 * Create a TopBorder.
	 * 
	 * @param color
	 *            The border color.
	 */
	public LeftBorder(final Color color) {
		super();
		this.color = color;
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
	 * 
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(0, 1, 0, 0);
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
	 *      java.awt.Insets)
	 * 
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = 1;
		insets.top = insets.bottom = insets.right = 0;
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
		g.drawLine(x, y, x, y + height - 1);
		g.setColor(oColor);
	}
}
