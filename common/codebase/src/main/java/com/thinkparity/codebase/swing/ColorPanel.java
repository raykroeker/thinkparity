/*
 * Jan 9, 2006
 */
package com.thinkparity.codebase.swing;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class ColorPanel extends AbstractJPanel {

	/**
	 * The background color of the panel.
	 * 
	 */
	private final Color color;

	/**
	 * Create a ColorPanel.
	 * 
	 * @param color
	 *            The color of the panel.
	 */
	public ColorPanel(final Color color) {
		super();
		this.color = color;
	}

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	protected void paintComponent(Graphics g) {
		final Color originalColor = g.getColor();
		g.setColor(color);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(originalColor);
	}
}
