/*
 * Dec 30, 2005
 */
package com.thinkparity.browser;


import com.thinkparity.browser.platform.Platform;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser2 {

	/**
	 * The browser platform.
	 * 
	 */
	private static Platform platform;

	/**
	 * Obtain the browser platform.
	 * 
	 * @return The browser platform.
	 */
	public static Platform getPlatform() { return platform; }

	/**
	 * Run Browser2
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) {
		platform = new Browser2Platform();
		platform.start();
	}

	/**
	 * Create a Browser2.
	 * 
	 */
	private Browser2() { super(); }
}
