/*
 * Jan 4, 2006
 */
package com.thinkparity.browser.javax.swing.plaf.parity.color;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.jvnet.substance.color.ColorScheme;

import com.thinkparity.browser.util.log4j.LoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class iTunesColorScheme implements ColorScheme {

	private static final Color DARK_COLOR;

	private static final Color EXTRA_LIGHT_COLOR;

	private static final Color FOREGROUND_COLOR;

	private static final Color LIGHT_COLOR;

	private static final Color MID_COLOR;

	private static final Color ULTRA_DARK_COLOR;
	
	private static final Color ULTRA_LIGHT_COLOR;

	static {
		FOREGROUND_COLOR = Color.BLACK;

		ULTRA_LIGHT_COLOR = new Color(196, 196, 195);
		EXTRA_LIGHT_COLOR = new Color(196, 196, 195);
		LIGHT_COLOR = new Color(175, 175, 175);
		MID_COLOR = new Color(175, 175, 175);
		DARK_COLOR = new Color(150, 149, 150);
		ULTRA_DARK_COLOR = new Color(150, 149, 150);
	}

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Create a ParityColorScheme.
	 * 
	 */
	public iTunesColorScheme() {
		super();
		logger.debug("foreground:" + FOREGROUND_COLOR.toString());
		logger.debug("ultra light:" + ULTRA_LIGHT_COLOR.toString());
		logger.debug("extra light:" + EXTRA_LIGHT_COLOR.toString());
		logger.debug("light:" + LIGHT_COLOR.toString());
		logger.debug("mid:" + MID_COLOR.toString());
		logger.debug("dark:" + DARK_COLOR.toString());
		logger.debug("ultra dark:" + ULTRA_DARK_COLOR.toString());
	}

	/**
	 * @see org.jvnet.substance.color.ColorScheme#getDarkColor()
	 */
	public Color getDarkColor() { return DARK_COLOR; }

	/**
	 * @see org.jvnet.substance.color.ColorScheme#getExtraLightColor()
	 */
	public Color getExtraLightColor() { return EXTRA_LIGHT_COLOR; }

	/**
	 * @see org.jvnet.substance.color.ColorScheme#getForegroundColor()
	 */
	public Color getForegroundColor() { return FOREGROUND_COLOR; }

	/**
	 * @see org.jvnet.substance.color.ColorScheme#getLightColor()
	 */
	public Color getLightColor() { return LIGHT_COLOR; }

	/**
	 * @see org.jvnet.substance.color.ColorScheme#getMidColor()
	 */
	public Color getMidColor() { return MID_COLOR; }

	/**
	 * @see org.jvnet.substance.color.ColorScheme#getUltraDarkColor()
	 */
	public Color getUltraDarkColor() { return ULTRA_DARK_COLOR; }

	/**
	 * @see org.jvnet.substance.color.ColorScheme#getUltraLightColor()
	 */
	public Color getUltraLightColor() { return ULTRA_LIGHT_COLOR; }

}
