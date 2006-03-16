/*
 * Mar 15, 2006
 */
package com.thinkparity.browser.javax.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.EtchedBorder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CustomEtechedBorder extends EtchedBorder {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a CustomEtechedBorder.
	 * @param etchType
	 * @param highlight
	 * @param shadow
	 */
	public CustomEtechedBorder(int etchType, Color highlight, Color shadow) {
		super(etchType, highlight, shadow);
	}

	/**
	 * @see javax.swing.border.EtchedBorder#getBorderInsets(java.awt.Component)
	 */
	public Insets getBorderInsets(Component c) {
        return new Insets(2, 0, 2, 2);
	}

	/**
	 * @see javax.swing.border.EtchedBorder#getBorderInsets(java.awt.Component, java.awt.Insets)
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = 0;
        insets.top = insets.right = insets.bottom = 2;
        return insets;
	}

	/**
	 * @see javax.swing.border.EtchedBorder#paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int)
	 */
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		int w = width;
		int h = height;
		
		g.translate(x, y);

		g.setColor(etchType == LOWERED? getShadowColor(c) : getHighlightColor(c));
		g.drawLine(0, 0, w-2, 0);		// Left to right on the top
		g.drawLine(w-2, 0, w-2, h-2);	// Top to bottom on the right
		g.drawLine(w-2, h-2, 0, h-2);	// Right to left on the bottom
		
		g.setColor(etchType == LOWERED? getHighlightColor(c) : getShadowColor(c));
		g.drawLine(0, 1, w-3, 1);		// Left to right on the top
		
		g.drawLine(0, h-1, w-1, h-1);	// Left to right on the bottom
		g.drawLine(w-1, h-1, w-1, 0);	// Bottom to top on the right

		g.translate(-x, -y);
	}
}
