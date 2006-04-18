/*
 * Jan 21, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

import com.thinkparity.browser.application.browser.BrowserConstants;

/**
 * Provides a border for the main cells; when the cell is "expanded".  The
 * border drawn here is simply the top; left and right lines.  The bottom
 * line is omitted.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see BrowserConstants#Colours#MAIN_CELL_DEFAULT_BORDER1
 * @see BrowserConstants#Colours#MAIN_CELL_DEFAULT_BORDER2
 */
public class Expanded extends AbstractBorder {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/** Create an Expanded. */
	public Expanded() { super(); }

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
	 * 
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(2, 1, 0, 1);
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
	 *      java.awt.Insets)
	 * 
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 2;
		insets.left = insets.right = 1;
        insets.bottom = 0;
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
        g.setColor(BrowserConstants.Colours.MAIN_CELL_DEFAULT_BORDER1);
        // top first color
        g.drawLine(x, y, x + width - 1, y);
        g.setColor(BrowserConstants.Colours.MAIN_CELL_DEFAULT_BORDER2);
        // top second color
        g.drawLine(x, y + 1, x + width - 1, y + 1);
        // left hand side
        g.drawLine(x, y + 1, x, y + height - 1);
        // right hand side
        g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 1);
		g.setColor(oColor);
	}
}
