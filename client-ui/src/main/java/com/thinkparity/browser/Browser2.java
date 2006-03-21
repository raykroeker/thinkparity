/*
 * Dec 30, 2005
 */
package com.thinkparity.browser;


import com.thinkparity.browser.platform.Browser2Platform;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Browser2 {

	/**
	 * Run Browser2
	 * 
	 * @param args
	 *            Command line arguments.
	 */
	public static void main(String[] args) { Browser2Platform.start(args); }

	/**
	 * Create a Browser2.
	 * 
	 */
	private Browser2() { super(); }
}
