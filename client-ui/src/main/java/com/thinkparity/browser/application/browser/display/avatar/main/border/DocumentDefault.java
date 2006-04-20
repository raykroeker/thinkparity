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
 * The separator border is a custom border that displays a white line at the top
 * of the component
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see BrowserConstants#Colours#MAIN_CELL_DEFAULT_BORDER1
 */
public class DocumentDefault extends AbstractBorder {

	/** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/** Create a Default. */
	public DocumentDefault() { super(); }

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component)
	 * 
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(1, 0, 0, 0);
	}

	/**
	 * @see javax.swing.border.AbstractBorder#getBorderInsets(java.awt.Component,
	 *      java.awt.Insets)
	 * 
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = 1;
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
        g.setColor(BrowserConstants.Colours.MAIN_CELL_DEFAULT_BORDER1);
        // top first color
        g.drawLine(x, y, x + width - 1, y);
		g.setColor(oColor);
	}
}
