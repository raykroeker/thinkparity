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
 * The last pseduo border is a simple left\right\bottom border.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see BrowserConstants#Colours#MAIN_CELL_DEFAULT_BORDER2
 */
public class HistoryItemLastPseudoBorder extends AbstractBorder {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/** Create a HistoryItemLastPseudoBorder. */
	public HistoryItemLastPseudoBorder() { super(); }

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
	 * 
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(0, 1, 1, 1);
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
	 *      java.awt.Insets)
	 * 
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 0;
        insets.left =  insets.bottom = insets.right = 1;
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
        g.setColor(BrowserConstants.Colours.MAIN_CELL_DEFAULT_BORDER2);
        // left hand side
        g.drawLine(x, y, x, y + height);
        // right hand side
        g.drawLine(x + width - 1, y, x + width - 1, y + height);
        // bottom
        g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
		g.setColor(oColor);
	}
}
