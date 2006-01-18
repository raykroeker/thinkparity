/*
 * Jan 18, 2006
 */
package com.thinkparity.browser;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserController {

	private static final BrowserController singleton;

	static {
		singleton = new BrowserController();
	}

	public static BrowserController getInstance() { return singleton; }

	private Browser browser;

	/**
	 * Create a BrowserController [Singleton]
	 * 
	 */
	private BrowserController() { super(); }

	public Browser getBrowser() { return null; }
}
