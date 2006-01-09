/*
 * Dec 14, 2005
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Color;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserColorUtil {

	/**
	 * Singleton instance.
	 */
	private static final BrowserColorUtil singleton;

	/**
	 * Synchronization lock.
	 */
	private static final Object singletonLock;

	static {
		singleton = new BrowserColorUtil();
		singletonLock = new Object();
	}

	public static Color getParityActiveBlue() {
		synchronized(singletonLock) { return singleton.getParityActiveBlueImpl(); }
	}
	public static Color getParityHotBlue() {
		synchronized(singletonLock) { return singleton.getParityHotBlueImpl(); }
	}
	public static Color getParitySplashBlue() {
		synchronized(singletonLock) { return singleton.getParitySplashBlueImpl(); }
	}
	public static Color getRed() {
		synchronized(singletonLock) { return singleton.getRedImpl(); }
	}
	public static Color getRGB(final Integer red, final Integer green, final Integer blue) {
		synchronized(singletonLock) { return singleton.getRGBImpl(red, green, blue); }
	}
	public static Color getWhite() {
		synchronized(singletonLock) { return singleton.getWhiteImpl(); }
	}

	/**
	 * Create a BrowserColorUtil [Singleton]
	 */
	private BrowserColorUtil() { super(); }

	private Color getParityActiveBlueImpl() { return new Color(179, 177, 248); }
	private Color getParityHotBlueImpl() { return new Color(0, 0, 204); }
	private Color getParitySplashBlueImpl() { return new Color(114, 146, 195); }
	private Color getRedImpl() { return Color.RED; }
	private Color getRGBImpl(final Integer red, final Integer green, final Integer blue) { return new Color(red, green, blue); }
	private Color getWhiteImpl() { return Color.WHITE; }
}
