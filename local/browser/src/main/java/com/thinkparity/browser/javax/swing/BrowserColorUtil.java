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

	public static Color getBlack() {
		synchronized(singletonLock) { return singleton.doGetBlack(); }
	}
	public static Color getOutlineClosed() {
		synchronized(singletonLock) { return singleton.doGetOutlineClosed(); }
	}
	public static Color getOutlineHasBeenSeen() {
		synchronized(singletonLock) { return singleton.doGetOutlineHasBeenSeen(); }
	}
	public static Color getOutlineHasNotBeenSeen() {
		synchronized(singletonLock) { return singleton.doGetOutlineHasNotBeenSeen(); }
	}
	public static Color getRGBColor(final int r, final int g, final int b,
			final int a) {
		synchronized(singletonLock) { return singleton.doGetRGB(r, g, b, a); }
	}
	public static Color getWhite() {
		synchronized(singletonLock) { return singleton.doGetWhite(); }
	}

	/**
	 * Create a BrowserColorUtil [Singleton]
	 */
	private BrowserColorUtil() { super(); }

	private Color doGetBlack() { return Color.BLACK; }
	private Color doGetOutlineClosed() { return doGetRGB(214, 217, 229, 255); }
	private Color doGetOutlineHasBeenSeen() { return doGetOutlineClosed(); }
	private Color doGetOutlineHasNotBeenSeen() { return doGetRGB(137, 156, 229, 255); }
	private Color doGetRGB(final int r, final int g, final int b, final int a) {
		return new Color(r, g, b, a);
	}
	private Color doGetWhite() { return Color.WHITE; }
}
