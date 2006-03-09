/*
 * Jan 27, 2006
 */
package com.thinkparity.browser.application.browser.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import com.thinkparity.browser.platform.application.display.Display;
import com.thinkparity.browser.platform.util.ImageIOUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class TitleDisplay extends Display {

	/**
	 * The display's background.
	 * 
	 */
	private static final Image BACKGROUND;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static { BACKGROUND = ImageIOUtil.read("TitleDisplay.png"); }

	/**
	 * Create a TitleDisplay.
	 * 
	 */
	public TitleDisplay() { super("TitleDisplay", Color.WHITE); }

	/**
	 * @see com.thinkparity.browser.platform.application.display.Display#getId()
	 * 
	 */
	public DisplayId getId() { return DisplayId.TITLE; }

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
