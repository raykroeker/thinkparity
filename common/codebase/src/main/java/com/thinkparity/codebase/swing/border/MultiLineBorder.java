/*
 * Jan 21, 2006
 */
package com.thinkparity.codebase.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

/**
 * Provides a multiple line border at the top of a component.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class MultiLineBorder extends AbstractBorder {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The colors to display.
	 * 
	 */
	private final Color[] colors;

	/**
	 * Create a MultiLineBorder.
	 */
	public MultiLineBorder(final Color[] colors) {
		super();
		this.colors = new Color[colors.length];
		System.arraycopy(colors, 0, this.colors, 0, colors.length);
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
	 * 
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(colors.length, 0, 0, 0);
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
	 *      java.awt.Insets)
	 * 
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = colors.length;
		insets.left = insets.bottom = insets.right = 0;
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
		for(int i = 0; i < colors.length; i++) {
			g.setColor(colors[i]);
			g.drawLine(x, y + i, x + width - 1, y + i);
		}
		g.setColor(oColor);
	}
}
