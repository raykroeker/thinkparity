/*
 * Jan 6, 2006
 */
package com.thinkparity.browser;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserBootstrap {

	static void bootstrap() {
		System.setProperty("parity.insecure", "true");
		System.setProperty("smack.debugEnabled", "true");
	}

	/**
	 * Create a BrowserBootstrap.
	 */
	BrowserBootstrap() { super(); }
}
