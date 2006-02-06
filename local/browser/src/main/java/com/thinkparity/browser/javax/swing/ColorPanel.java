/*
 * Jan 9, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ColorPanel extends JPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

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
