/*
 * Jan 28, 2006
 */
package com.thinkparity.browser.ui.display.avatar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TopBottomBorder extends AbstractBorder {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The color of the border.
	 * 
	 */
	private final Color color;

	/**
	 * Create a TopBottomBorder.
	 * 
	 */
	public TopBottomBorder(final Color color) {
		super();
		this.color = color;
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
	 *      java.awt.Insets)
	 * 
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = insets.bottom = 1;
		insets.left = insets.right = 0;
		return insets;
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
	 * 
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(1, 0, 1, 0);
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
		g.drawLine(x, y, x + width - 1, y);
		g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
		g.setColor(oColor);
	}
}
