/*
 * Jan 9, 2006
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserFontUtil {

	private static final BrowserFontUtil singleton;

	private static final Object singletonLock;

	static {
		singleton = new BrowserFontUtil();
		singletonLock = new Object();
	}

	/**
	 * Obtain the font metrics for a given font.
	 * 
	 * @param f
	 *            The font to retreive the metrics for.
	 * @return The font metrics.
	 */	
	public static FontMetrics getMetrics(final Font f) {
		synchronized(singletonLock) { return singleton.doGetMetrics(f); }
	}

	/**
	 * A dummy image used to obtain a graphics context.
	 * 
	 */
	private final BufferedImage dummyImage;

	/**
	 * Create a BrowserFontUtil.
	 */
	public BrowserFontUtil() {
		super();
		this.dummyImage =
			new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR_PRE);
	}

	/**
	 * Obtain the font metrics for a given font.
	 * 
	 * @param f
	 *            The font to retreive the metrics for.
	 * @return The font metrics.
	 */
	private FontMetrics doGetMetrics(final Font f) {
		final FontMetrics fm;
		final Graphics2D g = (Graphics2D) dummyImage.createGraphics();
		fm = g.getFontMetrics(f);
		g.dispose();
		return fm;
	}
}
