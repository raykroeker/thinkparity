/*
 * Jan 20, 2006
 */
package com.thinkparity.browser.application.browser.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import com.thinkparity.browser.javax.swing.border.MultiLineBorder;
import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class InfoDisplay extends Display {

	private static final Image BACKGROUND;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static { BACKGROUND = ImageIOUtil.read("InfoDisplay.png"); }

	/**
	 * Create an InfoDisplay.
	 * 
	 */
	InfoDisplay() {
		super("InfoDisplay", Color.WHITE);
		// BORDER InfoDisplay Multiline Top 137,139,142,255 1; 238,238,238,255 1; 
		setBorder(new MultiLineBorder(new Color[] {new Color(137, 139, 142, 255), new Color(238, 238, 238, 255)}));
	}

	/**
	 * @see com.thinkparity.browser.platform.application.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.INFO; }

	/**
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 * 
	 */
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		final Graphics2D g2 = (Graphics2D) g.create();
		try { g2.drawImage(BACKGROUND, getInsets().left, getInsets().top, this); }
		finally { g2.dispose(); }
	}
}
